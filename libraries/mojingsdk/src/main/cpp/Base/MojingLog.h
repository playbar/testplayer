﻿#ifndef MJ_Log_h
#define MJ_Log_h

#include "MojingTypes.h"
#include <stdarg.h>

#if defined MJ_OS_ANDROID
#include <android/log.h>
#define  LOG_TAG    __FILE__

#define  LOGI(format, args...) { fprintf(stderr, format, ##args); __android_log_print(ANDROID_LOG_INFO,  LOG_TAG, format, ##args); }
#define  LOGV(format, args...) { fprintf(stderr, format, ##args); __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, format, ##args); }
#define  LOGE(format, args...) { fprintf(stderr, format, ##args); __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, format, ##args); }
#define  LOGD(format, args...) { fprintf(stderr, format, ##args); __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, format, ##args); }
#endif

namespace Baofeng
{
	namespace Mojing
	{

//-----------------------------------------------------------------------------------
// ***** Logging Constants

// LogMaskConstants defined bit mask constants that describe what log messages
// should be displayed.
enum LogMaskConstants
{
    LogMask_Regular = 0x100,
    LogMask_Debug   = 0x200,
    LogMask_None    = 0,
    LogMask_All     = LogMask_Regular|LogMask_Debug
};


// LogMessageType describes the type of the log message, controls when it is
// displayed and what prefix/suffix is given to it. Messages are subdivided into
// regular and debug logging types. Debug logging is only generated in debug builds.
//
// Log_Text         - General output text displayed without prefix or new-line.
//                    Used in OVR libraries for general log flow messages
//                    such as "Device Initialized".
//
// Log_Error        - Error message output with "Error: %s\n", intended for
//                    application/sample-level use only, in cases where an expected
//                    operation failed. OVR libraries should not use this internally,
//                    reporting status codes instead.
//
// Log_DebugText    - Message without prefix or new lines; output in Debug build only.
//
// Log_Debug        - Debug-build only message, formatted with "Debug: %s\n".
//                    Intended to comment on incorrect API usage that doesn't lead
//                    to crashes but can be avoided with proper use.
//                    There is no Debug Error on purpose, since real errors should
//                    be handled by API user.
//
// Log_Assert      -  Debug-build only message, formatted with "Assert: %s\n".
//                    Intended for severe unrecoverable conditions in library
//                    source code. Generated though MJ_ASSERT_MSG(c, "Text").

enum LogMessageType
{    
    // General Logging
    Log_Text        = LogMask_Regular | 0,    
    Log_Error       = LogMask_Regular | 1, // "Error: %s\n".
    
    // Debug-only messages (not generated in release build)
    Log_DebugText   = LogMask_Debug | 0,
    Log_Debug       = LogMask_Debug | 1,   // "Debug: %s\n".
    Log_Assert      = LogMask_Debug | 2,   // "Assert: %s\n".
};


// LOG_VAARG_ATTRIBUTE macro, enforces printf-style fromatting for message types
#ifdef __GNUC__
#  define MJ_LOG_VAARG_ATTRIBUTE(a,b) __attribute__((format (printf, a, b)))
#else
#  define MJ_LOG_VAARG_ATTRIBUTE(a,b)
#endif


//-----------------------------------------------------------------------------------
// ***** Log

// Log defines a base class interface that can be implemented to catch both
// debug and runtime messages.
// Debug logging can be overridden by calling Log::SetGlobalLog.

class Log
{
    friend class System;
public: 
    Log(unsigned logMask = LogMask_Debug) : LoggingMask(logMask) { }
    virtual ~Log();

    // Log formating buffer size used by default LogMessageVarg. Longer strings are truncated.
    enum { MaxLogBufferMessageSize = 4096 };

    unsigned        GetLoggingMask() const            { return LoggingMask; }
    void            SetLoggingMask(unsigned logMask)  { LoggingMask = logMask; }

    // This virtual function receives all the messages,
    // developers should override this function in order to do custom logging
    virtual void    LogMessageVarg(LogMessageType messageType, const char* fmt, va_list argList);

    // Call the logging function with specific message type, with no type filtering.
    void            LogMessage(LogMessageType messageType,
                               const char* fmt, ...) MJ_LOG_VAARG_ATTRIBUTE(3,4);


    // Helper used by LogMessageVarg to format the log message, writing the resulting
    // string into buffer. It formats text based on fmt and appends prefix/new line
    // based on LogMessageType.
    static void     FormatLog(char* buffer, unsigned bufferSize, LogMessageType messageType,
                              const char* fmt, va_list argList);

    // Default log output implementation used by by LogMessageVarg.
    // Debug type may be used to re-direct output on some platforms, but doesn't
    // necessarily disable it in release builds; that is the job of the called.    
    static void     DefaultLogOutput(LogMessageType messageType,
                                     const char* formattedText);

    // Determines if the specified message type is for debugging only.
    static bool     IsDebugMessage(LogMessageType messageType)
    {
        return (messageType & LogMask_Debug) != 0;
    }

    // *** Global APIs

    // Global Log registration APIs.
    //  - Global log is used for MJ_DEBUG messages. Set global log to null (0)
    //    to disable all logging.
    static void     SetGlobalLog(Log *log);
    static Log*     GetGlobalLog();

    // Returns default log singleton instance.
    static Log*     GetDefaultLog();

    // Applies logMask to the default log and returns a pointer to it.
    // By default, only Debug logging is enabled, so to avoid SDK generating console
    // messages in user app (those are always disabled in release build,
    // even if the flag is set). This function is useful in System constructor.
    static Log*     ConfigureDefaultLog(unsigned logMask = LogMask_Debug)
    {
        Log* log = GetDefaultLog();
        log->SetLoggingMask(logMask);
        return log;
    }

private:
    // Logging mask described by LogMaskConstants.
    unsigned    LoggingMask;
};


//-----------------------------------------------------------------------------------
// ***** Global Logging Functions and Debug Macros

// These functions will output text to global log with semantics described by
// their LogMessageType.
void LogText(const char* fmt, ...) MJ_LOG_VAARG_ATTRIBUTE(1,2);
void LogError(const char* fmt, ...) MJ_LOG_VAARG_ATTRIBUTE(1,2);

#ifdef MJ_BUILD_DEBUG

    // Debug build only logging.
    void LogDebugText(const char* fmt, ...) MJ_LOG_VAARG_ATTRIBUTE(1,2);
    void LogDebug(const char* fmt, ...) MJ_LOG_VAARG_ATTRIBUTE(1,2);
    void LogAssert(const char* fmt, ...) MJ_LOG_VAARG_ATTRIBUTE(1,2);

    // Macro to do debug logging, printf-style.
    // An extra set of set of parenthesis must be used around arguments,
    // as in: MJ_LOG_DEBUG(("Value %d", 2)).
#define MJ_DEBUG_LOG(args)       do { LogDebug args; } while(0)
#define MJ_DEBUG_LOG_TEXT(args)  do { LogDebugText args; } while(0)

#define MJ_ASSERT_LOG(c, args)   do { if (!(c)) { LogAssert args; MJ_DEBUG_BREAK; } } while(0)

#else

    // If not in debug build, macros do nothing.
    #define MJ_DEBUG_LOG(args)         ((void)0)
    #define MJ_DEBUG_LOG_TEXT(args)    ((void)0)
    #define MJ_ASSERT_LOG(c, args)     ((void)0)

#endif

} // OVR 
}

#endif
