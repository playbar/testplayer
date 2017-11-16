﻿#include "MojingSensorFilter.h"

namespace Baofeng
{
	namespace Mojing
	{

		template <typename T>
		Vector3<T> SensorFilter<T>::Median() const
		{
			Vector3<T> result;
			T* slice = (T*)MJ_ALLOC(this->ElemCount * sizeof(T));

			for (int coord = 0; coord < 3; coord++)
			{
				for (int i = 0; i < this->ElemCount; i++)
					slice[i] = this->Data[i][coord];
				result[coord] = Alg::Median(ArrayAdaptor(slice, this->ElemCount));
			}

			MJ_FREE(slice);
			return result;
		}

		//  Only the diagonal of the covariance matrix.
		template <typename T>
		Vector3<T> SensorFilter<T>::Variance() const
		{
			Vector3<T> mean = this->Mean();
			Vector3<T> total;
			for (int i = 0; i < this->ElemCount; i++)
			{
				total.x += (this->Data[i].x - mean.x) * (this->Data[i].x - mean.x);
				total.y += (this->Data[i].y - mean.y) * (this->Data[i].y - mean.y);
				total.z += (this->Data[i].z - mean.z) * (this->Data[i].z - mean.z);
			}
			return total / (float) this->ElemCount;
		}

		template <typename T>
		Matrix3<T> SensorFilter<T>::Covariance() const
		{
			Vector3<T> mean = this->Mean();
			Matrix3<T> total;
			for (int i = 0; i < this->ElemCount; i++)
			{
				total.M[0][0] += (this->Data[i].x - mean.x) * (this->Data[i].x - mean.x);
				total.M[1][0] += (this->Data[i].y - mean.y) * (this->Data[i].x - mean.x);
				total.M[2][0] += (this->Data[i].z - mean.z) * (this->Data[i].x - mean.x);
				total.M[1][1] += (this->Data[i].y - mean.y) * (this->Data[i].y - mean.y);
				total.M[2][1] += (this->Data[i].z - mean.z) * (this->Data[i].y - mean.y);
				total.M[2][2] += (this->Data[i].z - mean.z) * (this->Data[i].z - mean.z);
			}
			total.M[0][1] = total.M[1][0];
			total.M[0][2] = total.M[2][0];
			total.M[1][2] = total.M[2][1];
			for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				total.M[i][j] /= (float) this->ElemCount;
			return total;
		}

		template <typename T>
		Vector3<T> SensorFilter<T>::PearsonCoefficient() const
		{
			Matrix3<T> cov = this->Covariance();
			Vector3<T> pearson;
			pearson.x = cov.M[0][1] / (sqrt(cov.M[0][0])*sqrt(cov.M[1][1]));
			pearson.y = cov.M[1][2] / (sqrt(cov.M[1][1])*sqrt(cov.M[2][2]));
			pearson.z = cov.M[2][0] / (sqrt(cov.M[2][2])*sqrt(cov.M[0][0]));
			return pearson;
		}
	}

} //namespace OVR
