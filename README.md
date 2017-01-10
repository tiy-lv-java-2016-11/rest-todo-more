# Rest Todo More API

## Description
We are going to enhance the [Todo API](https://github.com/tiy-lv-java-2016-11/todo-api) with a bit more in the way of security.

## Requirements
* If not already complete add a password field to `User`
* Like in class add the following:
	* Token field
	* Expiration field
	* `generateToken` method (use the same algorithm)
	* `regenerateToken` method
* Add a TokenController with the following
	* Login method
		* Takes a command object of username/password
		* Validate that the user exists
		* If the user does exists check password
		* If user doesn't exist create the user with the provided username and hash the password
		* Returns token if valid
		* Returns 401 if token expired
	* Regenerate method
		* Takes a command object of username/password
                * Validate that the user exists
                * If the user does exists check password
		* If user doesn't exist throw an error
* Make sure each entity gets its own controller for CRUD
* Change the write methods in both of the CRUD controllers to the following:
	* They should validate the token and return an error otherwise
	* Make sure that if the user is using PUT or DELETE they are the owner of the item
* Add validation to your command objects and/or entities
	* Make sure that all fields have a minimum length
	* For any fields that are required ensure they are `@NotNull`

## Hard Mode
* Write a full registration endpoint that will do the following:
	* Take all user input and validate it
	* Ensure that the username has not been taken in the database
	* Send back appropriate error messages and status codes
* Rewrite tests to ensure all new functionality is tested with appropriate error codes

## Nightmare Mode
* Figure out how to write a custom validator and implement one

## Resources
* [Github Repo](https://github.com/tiy-lv-java-2016-11/rest-todo-more)
