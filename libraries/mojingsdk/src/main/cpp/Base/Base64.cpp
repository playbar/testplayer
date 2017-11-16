#include "Base64.h"


namespace Baofeng
{
	namespace Mojing
	{
		int Base64::Decode(const char *pInBuffer, char *pOutBuffer)
		{
			int length = strlen(pInBuffer);
			if (pOutBuffer == 0)
				return length * 3 / 4;
			//�����
			const char DecodeTable[] =
			{
				(char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-1, (char)-1, (char)-2, (char)-2, (char)-1, (char)-2, (char)-2, (char)
				(char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)
				(char)-1, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)62, (char)-2, (char)-2, (char)-2, (char)63, (char)
				(char)52, (char)53, (char)54, (char)55, (char)56, (char)57, (char)58, (char)59, (char)60, (char)61, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)
				(char)-2, (char)0, (char)1, (char)2, (char)3, (char)4, (char)5, (char)6, (char)7, (char)8, (char)9, (char)10, (char)11, (char)12, (char)13, (char)14, (char)
				(char)15, (char)16, (char)17, (char)18, (char)19, (char)20, (char)21, (char)22, (char)23, (char)24, (char)25, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)
				(char)-2, (char)26, (char)27, (char)28, (char)29, (char)30, (char)31, (char)32, (char)33, (char)34, (char)35, (char)36, (char)37, (char)38, (char)39, (char)40, (char)
				(char)41, (char)42, (char)43, (char)44, (char)45, (char)46, (char)47, (char)48, (char)49, (char)50, (char)51, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)
				(char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)
				(char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)
				(char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)
				(char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)
				(char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)
				(char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)
				(char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)
				(char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2, (char)-2
			};
			int bin = 0, i = 0, pos = 0;
			char* pRet = pOutBuffer;
			const char *current = pInBuffer;
			char ch;
			while ((ch = *current++) != '\0' && length-- > 0)
			{
				if (ch == base64_pad) { // ��ǰһ���ַ��ǡ�=����
					/*
					��˵��һ������ڽ���ʱ��4���ַ�Ϊһ�����һ���ַ�ƥ�䡣
					����������
					1�����ĳһ��ƥ��ĵڶ����ǡ�=���ҵ������ַ����ǡ�=����˵������������ַ������Ϸ���ֱ�ӷ��ؿ�
					2�������ǰ��=�����ǵڶ����ַ����Һ�����ַ�ֻ�����հ׷�����˵�������������Ϸ������Լ�����
					*/
					if (*current != '=' && (i % 4) == 1) {
						return NULL;
					}
					continue;
				}
				ch = DecodeTable[ch];
				//�������Ҫ�������������в��Ϸ����ַ�
				if (ch < 0) { /* a space or some other separator character, we simply skip over */
					continue;
				}
				switch (i % 4)
				{
				case 0:
					bin = ch << 2;
					break;
				case 1:
					bin |= ch >> 4;
					*(pRet++) = bin;
					bin = (ch & 0x0f) << 4;
					break;
				case 2:
					bin |= ch >> 2;
					*(pRet++) = bin;
					bin = (ch & 0x03) << 6;
					break;
				case 3:
					bin |= ch;
					*(pRet++) = bin;
					break;
				}
				i++;
			}
			return pRet - pOutBuffer;
		}
	}
}