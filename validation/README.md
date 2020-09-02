# Validation

This is for validation following JSR-380 (supercedes JSR-303) or javax.validation.  Also known as Bean Validation 2.0.


## Dependency

In case your spring boot does not have this

```
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
```    

## Syntax

The most important part is this. The Set will contain all the failures, if any.

```
  Set<ConstraintViolation<Student>> violations = validator.validate(some_pojo);
```  

The ContraintViolation will contain pertinent info such as:

* error message
* invalid value
* object class
* property of the object that failed
  
### Reference Syntax

see https://docs.oracle.com/javaee/7/tutorial/bean-validation001.htm

## Examples

### Basic

This will cover programmatic validation without Spring dependencies.

All the packages will only use "javax.validation.*"

See [BasicValidationTest](./src/test/java/com/myproject/springboot/validation/BasicValidationTest.java)

#### Nested Object
 
Need to annotate with "@Valid"

```
    @Valid
    private List<Subject> subjects;
```
    
  

### Custom Validation

#### Steps

1. Define your POJO. 
    * See [Employer](./src/main/java/com/myproject/springboot/validation/domain/Employer.java) as the main class
    * See [Employee](./src/main/java/com/myproject/springboot/validation/domain/Employee.java) as the class to be validated
1. Define your custom annotation
    * See [ValidateEmployee](./src/main/java/com/myproject/springboot/validation/custom/ValidateEmployee.java)
    * You can restrict the use of annotation via the @Target
    * You can add an inner annotation if you want to use in type like collections
1. Define your custom validator
    * See [EmployeeValidator](./src/main/java/com/myproject/springboot/validation/custom/EmployeeValidator.java)
    *  The syntax is ConstraintValidator<annotation_name, object_type>

### Customizing Error Message

The annotations have a "message" attribute.  You can do the following approach:

* do not supply - will use the default in the annotation
* supply static one - will use the supplied value
* supply a dynamic one - will interpolate the data
Example:
```
 @Size(
            min = 5,
            max = 14,
            message = "The employer firstName '${validatedValue}' must be between {min} and {max} characters long"
    )
    private String firstName;
```


Read more on https://www.logicbig.com/tutorials/java-ee-tutorial/bean-validation/validation-messages.html
and https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#chapter-message-interpolation

#### Spring Integration

see https://www.baeldung.com/spring-custom-validation-message-source for how to use with Spring and customizing.

#### Internationalization (i18n)

The error messages are stored in ResourceBundle (search ValidationMessages files) under hibernate-validator.

##### Spring Integration

1. Define your message source, if needed. Only for custom resource bundles.
```
   @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean(MessageSource messageSource) {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }
```
1. Trigger the request by sending "Accept-Language" header
```
Accept-Language=fr
```
    
### Validation Gouping

This is supported

TODO

### Spring Web/ Boot Integration

#### Steps

1. Annotate your domain objects
1. Modify your RequestMapping by adding the @Valid annotation to the object
    Example:
    ```
    
        @RequestMapping(value = "/student", method = RequestMethod.POST)
        public ResponseEntity<Student> postProperties(@Valid @RequestBody Student student) {
    
            return new ResponseEntity<>(studentService.getStudent(), HttpStatus.CREATED);
        }
    ```  
    
1. Customize your ControllerAdvice
    
    Note: this will throw a MethodArgumentNotValidException
    
    See [BasicValidationTest](./src/main/java/com/myproject/springboot/validation/controller/GlobalExcptionHandler.java)


#### Test

See [ValidationControllerTest](./src/test/java/com/myproject/springboot/validation/controller/ValidationControllerTest.java)

Note: Although it is technically possible, the way the exceptions are transformed is not as informative as the plain validation way

##### @Validated

Use "@Validated" at class level to trigger spring auto validation.



## Reference

* https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single
