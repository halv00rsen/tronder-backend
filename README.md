
# Trøndr
Trøndr is an application which can store and display different dialects and distinct words for them.

## Trøndr-backend
The backend application is written in Java Spring.

## Installing the application
1. Create a .env file or other file where you can store properties. Fill in the keys which are shown in .evn.example.
2. Install a preferred database engine. 

### Installing Git hooks
```bash
./scripts/install-hooks.bash
```

## User management
The application uses AWS Cognito for user management. Some other user management can be used as well, as long as it uses JWT-tokens for verification. 
The application needs the modulus, exponent and name of the public key(s) from Cognito so the backend can verify that the token is signed from the identity provider. For more information see the following link: https://docs.aws.amazon.com/cognito/latest/developerguide/amazon-cognito-user-pools-using-tokens-verifying-a-jwt.html
All user information the backend application uses, comes from the JWT-token sent in each request.

## Running the application
```bash
./mvnw spring-boot:run
```

## Running tests
```bash
./mvnw test
```

## Pushing
When pushing to git, all tests are run by default. To avoid this behaviour:
```bash
git push --no-verify
```
