﻿#ifndef MJ_SensorFilter_h
#define MJ_SensorFilter_h

#include "../Base/MojingMath.h"
#include "../Base/MojingTimer.h"
#include "../Base/MojingDeque.h"
#include "../Base/MojingAlg.h"

namespace Baofeng
{
	namespace Mojing
	{

		// A base class for filters that maintains a buffer of sensor data taken over time and implements
		// various simple filters, most of which are linear functions of the data history.
		// Maintains the running sum of its elements for better performance on large capacity values
		template <typename T>
		class SensorFilterBase : public CircularBuffer<T>
		{
		protected:
			T RunningTotal;               // Cached sum of the elements

		public:
			SensorFilterBase(int capacity = CircularBuffer<T>::DefaultCapacity)
				: CircularBuffer<T>(capacity), RunningTotal()
			{
					this->Clear();
				};

			// The following methods are augmented to update the cached running sum value
			void PushBack(const T &e)
			{
				CircularBuffer<T>::PushBack(e);
				RunningTotal += e;
				if (this->End == 0)
				{
					// update the cached total to avoid error accumulation
					RunningTotal = T();
					for (int i = 0; i < this->ElemCount; i++)
						RunningTotal += this->Data[i];
				}
			}

			void PushFront(const T &e)
			{
				CircularBuffer<T>::PushFront(e);
				RunningTotal += e;
				if (this->Beginning == 0)
				{
					// update the cached total to avoid error accumulation
					RunningTotal = T();
					for (int i = 0; i < this->ElemCount; i++)
						RunningTotal += this->Data[i];
				}
			}

			T PopBack()
			{
				T e = CircularBuffer<T>::PopBack();
				RunningTotal -= e;
				return e;
			}

			T PopFront()
			{
				T e = CircularBuffer<T>::PopFront();
				RunningTotal -= e;
				return e;
			}

			void Clear()
			{
				CircularBuffer<T>::Clear();
				RunningTotal = T();
			}

			// Simple statistics
			T Total() const
			{
				return RunningTotal;
			}

			T Mean() const
			{
				return this->IsEmpty() ? T() : (Total() / (float) this->ElemCount);
			}

			T MeanN(int n) const
			{
				MJ_ASSERT(n > 0);
				MJ_ASSERT(this->Capacity >= n);
				T total = T();
				for (int i = 0; i < n; i++)
				{
					total += this->PeekBack(i);
				}
				return total / n;
			}

			// A popular family of smoothing filters and smoothed derivatives

			T SavitzkyGolaySmooth4()
			{
				MJ_ASSERT(this->Capacity >= 4);
				return this->PeekBack(0)*0.7f +
					this->PeekBack(1)*0.4f +
					this->PeekBack(2)*0.1f -
					this->PeekBack(3)*0.2f;
			}

			T SavitzkyGolaySmooth8() const
			{
				MJ_ASSERT(this->Capacity >= 8);
				return this->PeekBack(0)*0.41667f +
					this->PeekBack(1)*0.33333f +
					this->PeekBack(2)*0.25f +
					this->PeekBack(3)*0.16667f +
					this->PeekBack(4)*0.08333f -
					this->PeekBack(6)*0.08333f -
					this->PeekBack(7)*0.16667f;
			}

			T SavitzkyGolayDerivative4() const
			{
				MJ_ASSERT(this->Capacity >= 4);
				return this->PeekBack(0)*0.3f +
					this->PeekBack(1)*0.1f -
					this->PeekBack(2)*0.1f -
					this->PeekBack(3)*0.3f;
			}

			T SavitzkyGolayDerivative5() const
			{
				MJ_ASSERT(this->Capacity >= 5);
				return this->PeekBack(0)*0.2f +
					this->PeekBack(1)*0.1f -
					this->PeekBack(3)*0.1f -
					this->PeekBack(4)*0.2f;
			}

			T SavitzkyGolayDerivative12() const
			{
				MJ_ASSERT(this->Capacity >= 12);
				return this->PeekBack(0)*0.03846f +
					this->PeekBack(1)*0.03147f +
					this->PeekBack(2)*0.02448f +
					this->PeekBack(3)*0.01748f +
					this->PeekBack(4)*0.01049f +
					this->PeekBack(5)*0.0035f -
					this->PeekBack(6)*0.0035f -
					this->PeekBack(7)*0.01049f -
					this->PeekBack(8)*0.01748f -
					this->PeekBack(9)*0.02448f -
					this->PeekBack(10)*0.03147f -
					this->PeekBack(11)*0.03846f;
			}

			T SavitzkyGolayDerivativeN(int n) const
			{
				MJ_ASSERT(this->capacity >= n);
				int m = (n - 1) / 2;
				T result = T();
				for (int k = 1; k <= m; k++)
				{
					int ind1 = m - k;
					int ind2 = n - m + k - 1;
					result += (this->PeekBack(ind1) - this->PeekBack(ind2)) * (float)k;
				}
				float coef = 3.0f / (m*(m + 1.0f)*(2.0f*m + 1.0f));
				result = result*coef;
				return result;
			}

			T Median() const
			{
				T* copy = (T*)MJ_ALLOC(this->ElemCount * sizeof(T));
				T result = Alg::Median(ArrayAdaptor(copy));
				MJ_FREE(copy);
				return result;
			}
		};

		// This class maintains a buffer of sensor data taken over time and implements
		// various simple filters, most of which are linear functions of the data history.
		template <typename T>
		class SensorFilter : public SensorFilterBase<Vector3<T> >
		{
		public:
			SensorFilter(int capacity = SensorFilterBase<Vector3<T> >::DefaultCapacity) : SensorFilterBase<Vector3<T> >(capacity) { };

			// Simple statistics
			Vector3<T> Median() const;
			Vector3<T> Variance() const; // The diagonal of covariance matrix
			Matrix3<T> Covariance() const;
			Vector3<T> PearsonCoefficient() const;
		};

		typedef SensorFilter<float> SensorFilterf;
		typedef SensorFilter<double> SensorFilterd;

		// This filter operates on the values that are measured in the body frame and rotate with the device
		class SensorFilterBodyFrame : public SensorFilterBase<Vector3f>
		{
		private:
			// low pass filter gain
			float gain;
			// sum of squared norms of the values
			float runningTotalLengthSq;
			// cumulative rotation quaternion
			Quatf Q;
			// current low pass filter output
			Vector3f output;

			// make private so it isn't used by accident
			// in addition to the normal SensorFilterBase::PushBack, keeps track of running sum of LengthSq
			// for the purpose of variance computations
			void PushBack(const Vector3f &e)
			{
				runningTotalLengthSq += this->IsFull() ? (e.LengthSq() - this->PeekFront().LengthSq()) : e.LengthSq();
				SensorFilterBase<Vector3f>::PushBack(e);
				if (this->End == 0)
				{
					// update the cached total to avoid error accumulation
					runningTotalLengthSq = 0;
					for (int i = 0; i < this->ElemCount; i++)
						runningTotalLengthSq += this->Data[i].LengthSq();
				}
			}

		public:
			SensorFilterBodyFrame(int capacity = SensorFilterBase<Vector3f>::DefaultCapacity)
				: SensorFilterBase<Vector3f>(capacity), gain(2.5),
				runningTotalLengthSq(0), Q(), output()  { };

			// return the scalar variance of the filter values (rotated to be in the same frame)
			float Variance() const
			{
				return this->IsEmpty() ? 0 : (runningTotalLengthSq / this->ElemCount - this->Mean().LengthSq());
			}

			// return the scalar standard deviation of the filter values (rotated to be in the same frame)
			float StdDev() const
			{
				return sqrt(Variance());
			}

			// confidence value based on the stddev of the data (between 0.0 and 1.0, more is better)
			float Confidence() const
			{
				return Alg::Clamp(0.48f - 0.1f * logf(StdDev()), 0.0f, 1.0f) * this->ElemCount / this->Capacity;
			}

			// add a new element to the filter
			// takes rotation increment since the last update 
			// in order to rotate the previous value to the current body frame
			void Update(Vector3f value, float deltaT, Quatf deltaQ = Quatf())
			{
				if (this->IsEmpty())
				{
					output = value;
				}
				else
				{
					// rotate by deltaQ
					output = deltaQ.Inverted().Rotate(output);
					// apply low-pass filter
					output += (value - output) * gain * deltaT;
				}

				// put the value into the fixed frame for the stddev computation
				Q = Q * deltaQ;
				PushBack(Q.Rotate(output));
			}

			// returns the filter average in the current body frame
			Vector3f GetFilteredValue() const
			{
				return Q.Inverted().Rotate(this->Mean());
			}
		};

	} //namespace OVR
}

#endif // MJ_SensorFilter_h
