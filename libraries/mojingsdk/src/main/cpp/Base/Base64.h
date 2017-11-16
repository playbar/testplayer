#ifndef _BASE64_H_
#define _BASE64_H_
#include "MojingString.h"
/**
* Base64 ����/����
* ��ͬ��Base32.h�в��õı����ֵ䣬����ʹ��Base64����ʹ�õı�׼�ֵ�
*/
namespace Baofeng
{
	namespace Mojing
	{
		class Base64 
		{
		private:
			String _base64_table;
			static const char base64_pad = '=';
		public:
			Base64()
			{
				_base64_table = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"; /*����Base64����ʹ�õı�׼�ֵ�*/
			}
			int Decode(const char *pInBuffer, char *pOutBuffer);
		};
	}
}
#endif
