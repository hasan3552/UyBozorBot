@REM ----------------------------------------------------------------------------
@REM Copyright 2001-2004 The Apache Software Foundation.
@REM
@REM Licensed under the Apache License, Version 2.0 (the "License");
@REM you may not use this file except in compliance with the License.
@REM You may obtain a copy of the License at
@REM
@REM      http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing, software
@REM distributed under the License is distributed on an "AS IS" BASIS,
@REM WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM See the License for the specific language governing permissions and
@REM limitations under the License.
@REM ----------------------------------------------------------------------------
@REM

@echo off

set ERROR_CODE=0

:init
@REM Decide how to startup depending on the version of windows

@REM -- Win98ME
if NOT "%OS%"=="Windows_NT" goto Win9xArg

@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" @setlocal

@REM -- 4NT shell
if "%eval[2+2]" == "4" goto 4NTArgs

@REM -- Regular WinNT shell
set CMD_LINE_ARGS=%*
goto WinNTGetScriptDir

@REM The 4NT Shell from jp software
:4NTArgs
set CMD_LINE_ARGS=%$
goto WinNTGetScriptDir

:Win9xArg
@REM Slurp the command line arguments.  This loop allows for an unlimited number
@REM of arguments (up to the command line limit, anyway).
set CMD_LINE_ARGS=
:Win9xApp
if %1a==a goto Win9xGetScriptDir
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto Win9xApp

:Win9xGetScriptDir
set SAVEDIR=%CD%
%0\
cd %0\..\.. 
set BASEDIR=%CD%
cd %SAVEDIR%
set SAVE_DIR=
goto repoSetup

:WinNTGetScriptDir
set BASEDIR=%~dp0\..

:repoSetup


if "%JAVACMD%"=="" set JAVACMD=java

if "%REPO%"=="" set REPO=%BASEDIR%\repo

set CLASSPATH="%BASEDIR%"\etc;"%REPO%"\org\telegram\telegrambots\5.7.1\telegrambots-5.7.1.jar;"%REPO%"\org\telegram\telegrambots-meta\5.7.1\telegrambots-meta-5.7.1.jar;"%REPO%"\com\google\guava\guava\30.0-jre\guava-30.0-jre.jar;"%REPO%"\com\google\guava\failureaccess\1.0.1\failureaccess-1.0.1.jar;"%REPO%"\com\google\guava\listenablefuture\9999.0-empty-to-avoid-conflict-with-guava\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;"%REPO%"\com\google\code\findbugs\jsr305\3.0.2\jsr305-3.0.2.jar;"%REPO%"\com\google\errorprone\error_prone_annotations\2.3.4\error_prone_annotations-2.3.4.jar;"%REPO%"\com\google\j2objc\j2objc-annotations\1.3\j2objc-annotations-1.3.jar;"%REPO%"\com\fasterxml\jackson\core\jackson-annotations\2.13.0\jackson-annotations-2.13.0.jar;"%REPO%"\com\fasterxml\jackson\jaxrs\jackson-jaxrs-json-provider\2.13.0\jackson-jaxrs-json-provider-2.13.0.jar;"%REPO%"\com\fasterxml\jackson\jaxrs\jackson-jaxrs-base\2.13.0\jackson-jaxrs-base-2.13.0.jar;"%REPO%"\com\fasterxml\jackson\module\jackson-module-jaxb-annotations\2.13.0\jackson-module-jaxb-annotations-2.13.0.jar;"%REPO%"\com\fasterxml\jackson\core\jackson-core\2.13.0\jackson-core-2.13.0.jar;"%REPO%"\jakarta\xml\bind\jakarta.xml.bind-api\2.3.3\jakarta.xml.bind-api-2.3.3.jar;"%REPO%"\com\fasterxml\jackson\core\jackson-databind\2.13.0\jackson-databind-2.13.0.jar;"%REPO%"\org\glassfish\jersey\inject\jersey-hk2\2.32\jersey-hk2-2.32.jar;"%REPO%"\org\glassfish\jersey\core\jersey-common\2.32\jersey-common-2.32.jar;"%REPO%"\org\glassfish\hk2\osgi-resource-locator\1.0.3\osgi-resource-locator-1.0.3.jar;"%REPO%"\com\sun\activation\jakarta.activation\1.2.2\jakarta.activation-1.2.2.jar;"%REPO%"\org\glassfish\hk2\hk2-locator\2.6.1\hk2-locator-2.6.1.jar;"%REPO%"\org\glassfish\hk2\external\aopalliance-repackaged\2.6.1\aopalliance-repackaged-2.6.1.jar;"%REPO%"\org\glassfish\hk2\hk2-api\2.6.1\hk2-api-2.6.1.jar;"%REPO%"\org\glassfish\hk2\hk2-utils\2.6.1\hk2-utils-2.6.1.jar;"%REPO%"\org\javassist\javassist\3.25.0-GA\javassist-3.25.0-GA.jar;"%REPO%"\org\glassfish\jersey\media\jersey-media-json-jackson\2.32\jersey-media-json-jackson-2.32.jar;"%REPO%"\org\glassfish\jersey\ext\jersey-entity-filtering\2.32\jersey-entity-filtering-2.32.jar;"%REPO%"\org\glassfish\jersey\containers\jersey-container-grizzly2-http\2.32\jersey-container-grizzly2-http-2.32.jar;"%REPO%"\org\glassfish\hk2\external\jakarta.inject\2.6.1\jakarta.inject-2.6.1.jar;"%REPO%"\org\glassfish\grizzly\grizzly-http-server\2.4.4\grizzly-http-server-2.4.4.jar;"%REPO%"\org\glassfish\grizzly\grizzly-http\2.4.4\grizzly-http-2.4.4.jar;"%REPO%"\org\glassfish\grizzly\grizzly-framework\2.4.4\grizzly-framework-2.4.4.jar;"%REPO%"\jakarta\ws\rs\jakarta.ws.rs-api\2.1.6\jakarta.ws.rs-api-2.1.6.jar;"%REPO%"\org\glassfish\jersey\core\jersey-server\2.32\jersey-server-2.32.jar;"%REPO%"\org\glassfish\jersey\core\jersey-client\2.32\jersey-client-2.32.jar;"%REPO%"\org\glassfish\jersey\media\jersey-media-jaxb\2.32\jersey-media-jaxb-2.32.jar;"%REPO%"\jakarta\annotation\jakarta.annotation-api\1.3.5\jakarta.annotation-api-1.3.5.jar;"%REPO%"\jakarta\validation\jakarta.validation-api\2.0.2\jakarta.validation-api-2.0.2.jar;"%REPO%"\org\json\json\20180813\json-20180813.jar;"%REPO%"\org\apache\httpcomponents\httpclient\4.5.13\httpclient-4.5.13.jar;"%REPO%"\org\apache\httpcomponents\httpcore\4.4.13\httpcore-4.4.13.jar;"%REPO%"\commons-logging\commons-logging\1.2\commons-logging-1.2.jar;"%REPO%"\commons-codec\commons-codec\1.11\commons-codec-1.11.jar;"%REPO%"\org\apache\httpcomponents\httpmime\4.5.13\httpmime-4.5.13.jar;"%REPO%"\commons-io\commons-io\2.11.0\commons-io-2.11.0.jar;"%REPO%"\dev\inmo\tgbotapi.libraries.cache.admins.plagubot\0.0.14\tgbotapi.libraries.cache.admins.plagubot-0.0.14.jar;"%REPO%"\org\jetbrains\kotlinx\kotlinx-serialization-json\1.3.1\kotlinx-serialization-json-1.3.1.jar;"%REPO%"\org\jetbrains\kotlinx\kotlinx-serialization-json-jvm\1.3.1\kotlinx-serialization-json-jvm-1.3.1.jar;"%REPO%"\org\jetbrains\kotlin\kotlin-stdlib\1.5.31\kotlin-stdlib-1.5.31.jar;"%REPO%"\org\jetbrains\annotations\13.0\annotations-13.0.jar;"%REPO%"\org\jetbrains\kotlin\kotlin-stdlib-common\1.5.31\kotlin-stdlib-common-1.5.31.jar;"%REPO%"\org\jetbrains\kotlinx\kotlinx-serialization-core-jvm\1.3.1\kotlinx-serialization-core-jvm-1.3.1.jar;"%REPO%"\dev\inmo\tgbotapi.libraries.cache.admins.micro_utils\0.0.14\tgbotapi.libraries.cache.admins.micro_utils-0.0.14.jar;"%REPO%"\dev\inmo\micro_utils.repos.common\0.8.2\micro_utils.repos.common-0.8.2.jar;"%REPO%"\org\jetbrains\kotlinx\kotlinx-coroutines-core\1.5.2\kotlinx-coroutines-core-1.5.2.jar;"%REPO%"\org\jetbrains\kotlinx\kotlinx-coroutines-core-jvm\1.5.2\kotlinx-coroutines-core-jvm-1.5.2.jar;"%REPO%"\org\jetbrains\kotlin\kotlin-stdlib-jdk8\1.5.30\kotlin-stdlib-jdk8-1.5.30.jar;"%REPO%"\org\jetbrains\kotlin\kotlin-stdlib-jdk7\1.5.30\kotlin-stdlib-jdk7-1.5.30.jar;"%REPO%"\dev\inmo\micro_utils.pagination.common\0.8.2\micro_utils.pagination.common-0.8.2.jar;"%REPO%"\com\benasher44\uuid\0.3.1\uuid-0.3.1.jar;"%REPO%"\dev\inmo\tgbotapi.libraries.cache.admins.common\0.0.14\tgbotapi.libraries.cache.admins.common-0.0.14.jar;"%REPO%"\dev\inmo\tgbotapi\0.37.0\tgbotapi-0.37.0.jar;"%REPO%"\dev\inmo\tgbotapi.core\0.37.0\tgbotapi.core-0.37.0.jar;"%REPO%"\org\jetbrains\kotlinx\kotlinx-serialization-properties\1.3.0\kotlinx-serialization-properties-1.3.0.jar;"%REPO%"\org\jetbrains\kotlinx\kotlinx-serialization-properties-jvm\1.3.0\kotlinx-serialization-properties-jvm-1.3.0.jar;"%REPO%"\com\soywiz\korlibs\klock\klock\2.4.7\klock-2.4.7.jar;"%REPO%"\dev\inmo\micro_utils.crypto\0.8.1\micro_utils.crypto-0.8.1.jar;"%REPO%"\dev\inmo\micro_utils.common\0.8.1\micro_utils.common-0.8.1.jar;"%REPO%"\dev\inmo\micro_utils.coroutines\0.8.1\micro_utils.coroutines-0.8.1.jar;"%REPO%"\dev\inmo\micro_utils.serialization.base64\0.8.1\micro_utils.serialization.base64-0.8.1.jar;"%REPO%"\dev\inmo\micro_utils.serialization.encapsulator\0.8.1\micro_utils.serialization.encapsulator-0.8.1.jar;"%REPO%"\dev\inmo\micro_utils.serialization.typed_serializer\0.8.1\micro_utils.serialization.typed_serializer-0.8.1.jar;"%REPO%"\dev\inmo\micro_utils.language_codes\0.8.1\micro_utils.language_codes-0.8.1.jar;"%REPO%"\io\ktor\ktor-client-core\1.6.5\ktor-client-core-1.6.5.jar;"%REPO%"\io\ktor\ktor-http\1.6.5\ktor-http-1.6.5.jar;"%REPO%"\io\ktor\ktor-utils\1.6.5\ktor-utils-1.6.5.jar;"%REPO%"\io\ktor\ktor-io\1.6.5\ktor-io-1.6.5.jar;"%REPO%"\io\ktor\ktor-http-cio\1.6.5\ktor-http-cio-1.6.5.jar;"%REPO%"\org\jetbrains\kotlinx\atomicfu\0.16.3\atomicfu-0.16.3.jar;"%REPO%"\org\jetbrains\kotlinx\atomicfu-jvm\0.16.3\atomicfu-jvm-0.16.3.jar;"%REPO%"\dev\inmo\tgbotapi.api\0.37.0\tgbotapi.api-0.37.0.jar;"%REPO%"\dev\inmo\tgbotapi.utils\0.37.0\tgbotapi.utils-0.37.0.jar;"%REPO%"\dev\inmo\tgbotapi.behaviour_builder\0.37.0\tgbotapi.behaviour_builder-0.37.0.jar;"%REPO%"\dev\inmo\tgbotapi.behaviour_builder.fsm\0.37.0\tgbotapi.behaviour_builder.fsm-0.37.0.jar;"%REPO%"\dev\inmo\micro_utils.fsm.common\0.8.1\micro_utils.fsm.common-0.8.1.jar;"%REPO%"\org\slf4j\slf4j-api\1.7.36\slf4j-api-1.7.36.jar;"%REPO%"\org\slf4j\slf4j-simple\1.7.36\slf4j-simple-1.7.36.jar;"%REPO%"\org\postgresql\postgresql\42.2.18\postgresql-42.2.18.jar;"%REPO%"\org\checkerframework\checker-qual\3.5.0\checker-qual-3.5.0.jar;"%REPO%"\org\apache\poi\poi-ooxml\5.0.0\poi-ooxml-5.0.0.jar;"%REPO%"\org\apache\poi\poi\5.0.0\poi-5.0.0.jar;"%REPO%"\org\slf4j\jcl-over-slf4j\1.7.30\jcl-over-slf4j-1.7.30.jar;"%REPO%"\org\apache\commons\commons-collections4\4.4\commons-collections4-4.4.jar;"%REPO%"\org\apache\commons\commons-math3\3.6.1\commons-math3-3.6.1.jar;"%REPO%"\com\zaxxer\SparseBitSet\1.2\SparseBitSet-1.2.jar;"%REPO%"\org\apache\poi\poi-ooxml-lite\5.0.0\poi-ooxml-lite-5.0.0.jar;"%REPO%"\org\apache\xmlbeans\xmlbeans\4.0.0\xmlbeans-4.0.0.jar;"%REPO%"\org\apache\commons\commons-compress\1.20\commons-compress-1.20.jar;"%REPO%"\com\github\virtuald\curvesapi\1.06\curvesapi-1.06.jar;"%REPO%"\org\bouncycastle\bcpkix-jdk15on\1.68\bcpkix-jdk15on-1.68.jar;"%REPO%"\org\bouncycastle\bcprov-jdk15on\1.68\bcprov-jdk15on-1.68.jar;"%REPO%"\org\apache\santuario\xmlsec\2.2.1\xmlsec-2.2.1.jar;"%REPO%"\com\fasterxml\woodstox\woodstox-core\5.2.1\woodstox-core-5.2.1.jar;"%REPO%"\org\codehaus\woodstox\stax2-api\4.2\stax2-api-4.2.jar;"%REPO%"\org\apache\xmlgraphics\batik-all\1.13\batik-all-1.13.jar;"%REPO%"\org\apache\xmlgraphics\batik-anim\1.13\batik-anim-1.13.jar;"%REPO%"\org\apache\xmlgraphics\batik-shared-resources\1.13\batik-shared-resources-1.13.jar;"%REPO%"\xml-apis\xml-apis-ext\1.3.04\xml-apis-ext-1.3.04.jar;"%REPO%"\org\apache\xmlgraphics\batik-awt-util\1.13\batik-awt-util-1.13.jar;"%REPO%"\org\apache\xmlgraphics\xmlgraphics-commons\2.4\xmlgraphics-commons-2.4.jar;"%REPO%"\org\apache\xmlgraphics\batik-bridge\1.13\batik-bridge-1.13.jar;"%REPO%"\org\apache\xmlgraphics\batik-codec\1.13\batik-codec-1.13.jar;"%REPO%"\org\apache\xmlgraphics\batik-constants\1.13\batik-constants-1.13.jar;"%REPO%"\org\apache\xmlgraphics\batik-css\1.13\batik-css-1.13.jar;"%REPO%"\org\apache\xmlgraphics\batik-dom\1.13\batik-dom-1.13.jar;"%REPO%"\xalan\xalan\2.7.2\xalan-2.7.2.jar;"%REPO%"\xalan\serializer\2.7.2\serializer-2.7.2.jar;"%REPO%"\xml-apis\xml-apis\1.4.01\xml-apis-1.4.01.jar;"%REPO%"\org\apache\xmlgraphics\batik-ext\1.13\batik-ext-1.13.jar;"%REPO%"\org\apache\xmlgraphics\batik-extension\1.13\batik-extension-1.13.jar;"%REPO%"\org\apache\xmlgraphics\batik-gui-util\1.13\batik-gui-util-1.13.jar;"%REPO%"\org\apache\xmlgraphics\batik-gvt\1.13\batik-gvt-1.13.jar;"%REPO%"\org\apache\xmlgraphics\batik-i18n\1.13\batik-i18n-1.13.jar;"%REPO%"\org\apache\xmlgraphics\batik-parser\1.13\batik-parser-1.13.jar;"%REPO%"\org\apache\xmlgraphics\batik-rasterizer-ext\1.13\batik-rasterizer-ext-1.13.jar;"%REPO%"\org\apache\xmlgraphics\batik-rasterizer\1.13\batik-rasterizer-1.13.jar;"%REPO%"\org\apache\xmlgraphics\batik-script\1.13\batik-script-1.13.jar;"%REPO%"\org\apache\xmlgraphics\batik-slideshow\1.13\batik-slideshow-1.13.jar;"%REPO%"\org\apache\xmlgraphics\batik-squiggle-ext\1.13\batik-squiggle-ext-1.13.jar;"%REPO%"\org\apache\xmlgraphics\batik-squiggle\1.13\batik-squiggle-1.13.jar;"%REPO%"\org\apache\xmlgraphics\batik-svg-dom\1.13\batik-svg-dom-1.13.jar;"%REPO%"\org\apache\xmlgraphics\batik-svgbrowser\1.13\batik-svgbrowser-1.13.jar;"%REPO%"\org\apache\xmlgraphics\batik-svggen\1.13\batik-svggen-1.13.jar;"%REPO%"\org\apache\xmlgraphics\batik-svgpp\1.13\batik-svgpp-1.13.jar;"%REPO%"\org\apache\xmlgraphics\batik-svgrasterizer\1.13\batik-svgrasterizer-1.13.jar;"%REPO%"\org\apache\xmlgraphics\batik-swing\1.13\batik-swing-1.13.jar;"%REPO%"\org\apache\xmlgraphics\batik-transcoder\1.13\batik-transcoder-1.13.jar;"%REPO%"\org\apache\xmlgraphics\batik-util\1.13\batik-util-1.13.jar;"%REPO%"\org\apache\xmlgraphics\batik-ttf2svg\1.13\batik-ttf2svg-1.13.jar;"%REPO%"\org\apache\xmlgraphics\batik-xml\1.13\batik-xml-1.13.jar;"%REPO%"\de\rototor\pdfbox\graphics2d\0.30\graphics2d-0.30.jar;"%REPO%"\org\apache\pdfbox\pdfbox\2.0.22\pdfbox-2.0.22.jar;"%REPO%"\org\apache\pdfbox\fontbox\2.0.22\fontbox-2.0.22.jar;"%REPO%"\org\projectlombok\lombok\1.18.16\lombok-1.18.16.jar;"%REPO%"\com\company\HomeMarket\17\HomeMarket-17.jar
set EXTRA_JVM_ARGUMENTS=
goto endInit

@REM Reaching here means variables are defined and arguments have been captured
:endInit

%JAVACMD% %JAVA_OPTS% %EXTRA_JVM_ARGUMENTS% -classpath %CLASSPATH_PREFIX%;%CLASSPATH% -Dapp.name="HomeMarket" -Dapp.repo="%REPO%" -Dbasedir="%BASEDIR%" Main %CMD_LINE_ARGS%
if ERRORLEVEL 1 goto error
goto end

:error
if "%OS%"=="Windows_NT" @endlocal
set ERROR_CODE=1

:end
@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" goto endNT

@REM For old DOS remove the set variables from ENV - we assume they were not set
@REM before we started - at least we don't leave any baggage around
set CMD_LINE_ARGS=
goto postExec

:endNT
@endlocal

:postExec

if "%FORCE_EXIT_ON_ERROR%" == "on" (
  if %ERROR_CODE% NEQ 0 exit %ERROR_CODE%
)

exit /B %ERROR_CODE%
