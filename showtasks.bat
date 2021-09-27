call runcrud.bat

if "%ERRORLEVEL%" == "0" goto openurl
echo.
echo Error occurred while executing script.
goto fail

:openurl
start chrome --new-window "http://localhost:8080/crud/v1/task/getTasks"
goto end

:fail
echo.
echo There were errors while compiling script.

:end
echo.
echo Script finished.