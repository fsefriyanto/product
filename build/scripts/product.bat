@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  product startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and PRODUCT_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\product-1.0.0-SNAPSHOT.jar;%APP_HOME%\lib\vertx-config-3.8.2.jar;%APP_HOME%\lib\vertx-web-3.8.2.jar;%APP_HOME%\lib\vertx-rx-java2-3.8.2.jar;%APP_HOME%\lib\vertx-kafka-client-3.8.2.jar;%APP_HOME%\lib\vertx-jdbc-client-3.8.2.jar;%APP_HOME%\lib\vertx-sql-common-3.8.2.jar;%APP_HOME%\lib\vertx-web-common-3.8.2.jar;%APP_HOME%\lib\vertx-auth-common-3.8.2.jar;%APP_HOME%\lib\vertx-rx-gen-3.8.2.jar;%APP_HOME%\lib\vertx-core-3.8.2.jar;%APP_HOME%\lib\postgresql-9.4-1201-jdbc41.jar;%APP_HOME%\lib\HikariCP-2.4.0.jar;%APP_HOME%\lib\rxjava2-jdbc-0.2.5.jar;%APP_HOME%\lib\kafka-streams-2.3.0.jar;%APP_HOME%\lib\kafka_2.12-2.3.0.jar;%APP_HOME%\lib\connect-json-2.3.0.jar;%APP_HOME%\lib\connect-api-2.3.0.jar;%APP_HOME%\lib\kafka-clients-2.3.0.jar;%APP_HOME%\lib\slf4j-simple-1.7.5.jar;%APP_HOME%\lib\slf4j-log4j12-1.7.21.jar;%APP_HOME%\lib\rxjava2-pool-0.2.5.jar;%APP_HOME%\lib\metrics-core-2.2.0.jar;%APP_HOME%\lib\scala-logging_2.12-3.9.0.jar;%APP_HOME%\lib\zkclient-0.11.jar;%APP_HOME%\lib\zookeeper-3.4.14.jar;%APP_HOME%\lib\slf4j-api-1.7.26.jar;%APP_HOME%\lib\netty-handler-proxy-4.1.39.Final.jar;%APP_HOME%\lib\netty-codec-http2-4.1.39.Final.jar;%APP_HOME%\lib\netty-codec-http-4.1.39.Final.jar;%APP_HOME%\lib\netty-handler-4.1.39.Final.jar;%APP_HOME%\lib\netty-resolver-dns-4.1.39.Final.jar;%APP_HOME%\lib\netty-codec-socks-4.1.39.Final.jar;%APP_HOME%\lib\netty-codec-dns-4.1.39.Final.jar;%APP_HOME%\lib\netty-codec-4.1.39.Final.jar;%APP_HOME%\lib\netty-transport-4.1.39.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.39.Final.jar;%APP_HOME%\lib\netty-resolver-4.1.39.Final.jar;%APP_HOME%\lib\netty-common-4.1.39.Final.jar;%APP_HOME%\lib\vertx-codegen-3.8.2.jar;%APP_HOME%\lib\jackson-module-scala_2.12-2.9.9.jar;%APP_HOME%\lib\jackson-dataformat-csv-2.9.9.jar;%APP_HOME%\lib\jackson-datatype-jdk8-2.9.9.jar;%APP_HOME%\lib\jackson-module-paranamer-2.9.9.jar;%APP_HOME%\lib\jackson-databind-2.9.9.1.jar;%APP_HOME%\lib\jackson-core-2.9.9.jar;%APP_HOME%\lib\c3p0-0.9.5.4.jar;%APP_HOME%\lib\vertx-bridge-common-3.8.2.jar;%APP_HOME%\lib\rxjava-2.2.12.jar;%APP_HOME%\lib\reactive-streams-1.0.3.jar;%APP_HOME%\lib\log4j-1.2.17.jar;%APP_HOME%\lib\commons-io-2.6.jar;%APP_HOME%\lib\guava-mini-0.1.2.jar;%APP_HOME%\lib\annotations-3.0.1.jar;%APP_HOME%\lib\zstd-jni-1.4.0-1.jar;%APP_HOME%\lib\lz4-java-1.6.0.jar;%APP_HOME%\lib\snappy-java-1.1.7.3.jar;%APP_HOME%\lib\rocksdbjni-5.18.3.jar;%APP_HOME%\lib\jackson-annotations-2.9.9.jar;%APP_HOME%\lib\mchange-commons-java-0.2.15.jar;%APP_HOME%\lib\jopt-simple-5.0.4.jar;%APP_HOME%\lib\scala-reflect-2.12.8.jar;%APP_HOME%\lib\jcip-annotations-1.0.jar;%APP_HOME%\lib\spotbugs-annotations-3.1.9.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\mvel2-2.3.1.Final.jar;%APP_HOME%\lib\audience-annotations-0.5.0.jar;%APP_HOME%\lib\paranamer-2.8.jar

@rem Execute product
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %PRODUCT_OPTS%  -classpath "%CLASSPATH%" io.vertx.core.Launcher %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable PRODUCT_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%PRODUCT_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
