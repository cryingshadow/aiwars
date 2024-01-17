:: The init-script.sh script should be executed when the project is initially set up.
:: It copies the pre-commit hooks into the hooks folder.
:: The pre-commit hook protects you from checking in faulty/non-executable code into
:: all branches of the repository.

xcopy "hooks\pre-commit" ".git\hooks" /Y