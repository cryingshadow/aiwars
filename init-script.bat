:: The init-script.sh or init-script.bat script should be executed when the project is initially set up.
:: It copies the pre-commit hook into the hooks folder.
:: The pre-commit hook protects you from checking in faulty/non-executable code into all branches of the repository
:: if the configured automated tests fail.

xcopy "hooks\pre-commit" ".git\hooks" /Y