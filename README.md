# Check the deployed app [Here.](https://eshop-alma-csui-adpro-2024.koyeb.app/)

# Reflection 1

<hr>

Q: You already implemented two new features using Spring Boot. Check again your source code and evaluate the coding standards that you have learned in this module. Write clean code principles and secure coding practices that have been applied to your code.  If you find any mistake in your source code, please explain how to improve your code. 

## Coding Standards Implemented
1. **Meaningful names:** In this exercise, I've named all my variables according to its use and purpose. I believe my naming system is clear and sufficiently meaningful.
2. **Functions:** The functions I use are short, specific, and named appropriately according to what it does.
3. **Comments:** The code is clear enough to not require comments. I believe this fits the criteria of "Explaining myself in Code".
4. **Error Handling:** I've implemented Error Handling to deal with possible invalid inputs. These errors will throw an exception early on in the process, making it easier to immediately identify them.
5. **Null:** None of my functions return null nor pass null.
6. **Input Data Validation:** In the Create Product page, I added the "type" and "required" specification in the HTML form to ensure that all input will be valid.

## Mistakes to Improve
1. So far, my code lacks any form of authentication and authorization, giving all users free reign to do anything they want. This lack of security could be improved.
2. Since product IDs are assigned sequentially, it's easy to guess and approximate the ID of products and delete them immediately by adding "/delete/{id}" into the URL. I believe this aspect could also be improved, perhaps by randomly generating unique IDs for each product.
3. My homepage is still very lacking.

<br>

# Reflection 2

<hr>

Q: After writing the unit test, how do you feel? How many unit tests should be made in a class? How to make sure that our unit tests are enough to verify our program? It would be good if you learned about code coverage. Code coverage is a metric that can help you understand how much of your source is tested. If you have 100% code coverage, does that mean your code has no bugs or errors?


**Answer:**
Writing unit tests made me feel more assured that my code actually functions properly. I believe that the appropriate number of unit tests to be made for a class depends entirely on the possible use cases for that class. The more ways it can go wrong, the more unit tests it'll need.

Though 100% code coverage is good, I don't think it guarantees that the code is free from bugs or errors. Code coverage shows how much of the source code is tested, but that doesn't guarantee that all possible errors, edge cases, and faulty logic routes have been tested as well. Rather than striving for 100% code coverage while sacrificing the quality of tests, I think it's better to write relevant tests that consider many possible cases instead.

<br>


Q: Suppose that after writing the CreateProductFunctionalTest.java along with the corresponding test case, you were asked to create another functional test suite that verifies the number of items in the product list. You decided to create a new Java class similar to the prior functional test suites with the same setup procedures and instance variables.
What do you think about the cleanliness of the code of the new functional test suite? Will the new code reduce the code quality? Identify the potential clean code issues, explain the reasons, and suggest possible improvements to make the code cleaner!

**Answer:**
I think it wouldn't be very clean and would reduce code quality to do this since it's redundant to make a separate suite despite requiring the same setup. For a more efficient alternative, it would be better to broaden the CreateProductFunctionalTest.java into a ProductFunctionalTest.java where both tests of creating products and tests of verifying product amounts could be housed in one place.

<br>

# Reflection 3

<hr>

Q: List the code quality issue(s) that you fixed during the exercise and explain your strategy on fixing them.

**Answer:**
Most of the code quality issues that I encountered using PMD were naming issues, unused import issues, and unnecessary modifier issues. These were the issues I fixed:
- The JUnit 5 test method name 'createProductPostTest_ShouldRedirectToProductList' doesn't match '[a-z][a-zA-Z0-9]*'
- The JUnit 5 test method name 'editProductPostTest_ShouldRedirectToProductList' doesn't match '[a-z][a-zA-Z0-9]*'

Though I’ve only fixed a few of the naming issues thus far, my strategy on fixing the rest is to follow the recommended course of actions from PMD, such as renaming my methods and removing unused lines. I also had an issue on braces, where PMD suggested I enforce a policy for braces on control statements. If I were to fix it, I would follow its suggestions there as well.

<br>

Q: Look at your CI/CD workflows (GitHub)/pipelines (GitLab). Do you think the current implementation has met the definition of Continuous Integration and Continuous Deployment? Explain the reasons (minimum 3 sentences)!

**Answer:**
I believe my implementation has met the definition of CI/CD. Though I wasn’t able to make my own workflow for deployment, Koyeb has already handled that through its features, so with every push to the main branch it’ll always re-deploy. My tests have been automated and have a decent percentage of coverage too, covering everything other than the main method in the EshopApplication. The scorecard analysis and PMD analysis have been automated through GitHub workflows as well.

<br>

# Reflection 4 (Module 3)

<hr>


1) Explain what principles you apply to your project!

**Answer:**
I believe I've implemented all SOLID principles in my project. These implementations are as follows:
- Single Responsibility Principle (SRP): At first, the controllers for the home page, the product pages, and the car pages were all kept in the same file: ProductController.java. I believe this violates the SRP since all of these controllers have very different functions and shouldn't be kept under one roof. To apply SRP, I simply decomposed them into 3 separate files: HomeController.java, CarController.java, and ProductController.java.
- Open-Closed Principle (OCP): With the existence of extendable classes such as the Car and Product models, along with the CarService and ProductService interfaces, I believe this project already fulfills the OCP requirement of being open for extension but closed for modification.
- Liskov Substitution Principle (LSP): This project has fulfilled LSP with CarServiceImpl and ProductServiceImpl being substitutable for CarService and ProductService.
- Interface Segregation Principle (ISP): This project has fulfilled ISP by splitting CarService and ProductService into separate interfaces, since each has its own implementations separate from one another.
- Dependency Inversion Principle (DIP): At first, CarController.java used the concrete implementation of CarServiceImpl rather than its interface, which violates the DIP. To fix this, I simply changed CarServiceImpl into CarService instead.

<br>

2) Explain the advantages of applying SOLID principles to your project with examples.

**Answer:**
- Maintainability: By applying SOLID principles, especially SRP, it becomes easier to maintain projects since every class is clearly separated by its function. I.e. if something goes wrong when users are trying to access the homepage, the developer only needs to check the HomeController.java to see what the problem is, rather than digging around in ProductController.java for the homepage routing.
- Readability: By applying SOLID principles, especially ISP, it's much easier to see what a class is supposed to do when reading its interface. I.e. if the ProductService and CarService interfaces were merged, it'd be harder to guess what the object implementing this interface would be like and what it would be used for.
- Security: By applying SOLID principles, especially OCP, the use of classes becomes much safer without much risk of accidentally causing ripple effects within the entire project by modifying a pre-existing class. I.e. new classes can extend the Product model and functionalities we've already made for the Product model would still work just fine.

<br>

3) Explain the disadvantages of not applying SOLID principles to your project with examples.

**Answer:**
- Hard to Debug: Without SOLID principles, especially SRP, it would be a lot harder to pinpoint what to fix when something goes wrong since a class can be responsible for multiple things at once. I.e. if tests for ProductController.java went wrong while it housed HomeController and CarController as well, it could be any of all 3 that went wrong.
- Unpredictability: Without SOLID principles, especially LSP, the program might behave unpredictably and cause problems when using subclasses that deviate from their superclass. I.e. if there was a subclass of Product called FancyProduct which returned its name instead of its id when using getProductId(), the entire product-page functionality will fall apart when FancyProduct objects are used.
- Lack of Flexibility: Without SOLID principles, especially DIP, a change in the concrete implementation can cause a ripple effect throughout the entire project, requiring accompanying changes to other parts of the project that use it. I.e. if CarServiceImpl didn't implement its interface and was used in CarController.java, parts of CarServiceImpl used in CarController could be deleted without notice and cause the entire project to crash.

<br>

# Reflection 5 (Module 4)

<hr>

1) Reflect based on Percival (2017) proposed self-reflective questions (in “Principles and Best Practice of Testing” submodule,
chapter “Evaluating Your Testing Objectives”), whether this TDD flow is useful enough for you or not.
If not, explain things that you need to do next time you make more tests.

**Answer:**
I feel that this TDD flow is very useful for me, since it keeps me on track about what functionalities I need to make and how to properly implement them.
I feel that it also gets rid of bias since sometimes when creating tests after implementations, I find myself making the tests to fit my implementation instead of the other way around.


2) You have created unit tests in Tutorial. Now reflect whether your tests have successfully followed F.I.R.S.T. principle or not. If not, explain things that you need to do the next time you create more tests.

**Answer:**
I believe they do abide by F.I.R.S.T. principles since they're fast, isolated, repeatable, self-validating, and thorough.
I also feel like i've already made as few tests as possible to cover all paths.
