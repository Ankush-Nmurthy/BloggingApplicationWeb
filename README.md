# BloggingApplicationWeb
### Description
Welcome to Blog Sculpture – your all-in-one platform for crafting, managing, and sharing captivating blogs. Designed with a powerful suite of functionalities and a robust tech stack, Blog Sculpture offers an unparalleled blogging experience for both users and administrators.

<img src="https://i.ibb.co/tKsLLJr/Screenshot-2023-12-04-091055.png" alt="Screenshot-2023-12-04-091055" border="0">


### Key Features:

**User Registration & Authentication:** Seamlessly sign up and log in securely to your personalized user dashboard. Protect your content and manage your blogs with confidence through our robust authentication system.

**Admin Page & Dashboard:** Administrators have access to a dedicated admin page and dashboard, empowering them to oversee and moderate the platform efficiently. Manage user accounts, oversee blog activity, and ensure a smooth blogging experience for all.

**Blog Home Page:** Explore an inviting blog home page showcasing a diverse array of engaging content from our community of writers. Discover new perspectives and stories that resonate with you.

**Create, Edit, and Delete Blogs:** Craft your narratives effortlessly. Create, edit, and delete your blogs using our intuitive interface, offering a user-friendly experience that supports your creative process.

**Like and Interact:** Engage with your favorite blogs by liking and interacting with them. Foster a sense of community and appreciation among fellow writers and readers.

**User Dashboard:** A personalized dashboard for users to manage their blogs, track engagement, and monitor their blog's performance. Gain insights and make data-driven decisions to enhance your content strategy.

**Secure User Blogs:** Safeguard your content with our robust security measures. Each user's blog is protected, ensuring privacy and control over your published work.

**Tech Stack:** Java, Spring Boot, Spring Data JPA, Thymeleaf, and MySQL, Blog Sculpture combines cutting-edge technologies to deliver a seamless, high-performance blogging platform.

## Database
<img src="https://i.ibb.co/3hpZtQP/Screenshot-2023-12-18-200621.png" alt="Screenshot-2023-12-18-200621" border="0" width="700px">

## Project Configuration

The project uses the following configuration for the Spring Boot application:

```properties
server.port = 9090 {you can also set your default port here}
#To configuer your own server port please follow the path provided below and change the server.port value;
#Create a database of your choice and replace in spring.datasource.url=jdbc:mysql://localhost:3306/{here}

spring.datasource.driver=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/growgarden
spring.datasource.username= Your Username
spring.datasource.password= Your Password

spring.jpa.hibernate.ddl-auto=update

```
## Setup

To run the application, follow these steps:

1. Ensure you have Java and MySQL installed on your system.

2. Set up the database with the provided connection details in the `application.properties`.

3. Run the Spring Boot application.

4. Access the application using the specified port (e.g., http://localhost:9090).
