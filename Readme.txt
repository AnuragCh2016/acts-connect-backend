ACTS Connect is a social media and alumni networking application designed to enable students to connect with each other and with alumni of the ACTS Organization. The application aims to address the challenge of connecting with seniors and former ACTS graduates who can assist current students not only with academics but also with referrals and industry advice.



Project Structure (Example) :
ActsConnectBackend/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── connect/
│   │   │           └── acts/
│   │   │               └── ActsConnectBackend/
│   │   │                   ├── model/
│   │   │                   │   ├── BatchSemester.java
│   │   │                   │   ├── Comment.java
│   │   │                   │   ├── Course.java
│   │   │                   │   ├── Job.java
│   │   │                   │   ├── JobStatus.java
│   │   │                   │   ├── JobType.java
│   │   │                   │   ├── Post.java
│   │   │                   │   ├── User.java
│   │   │                   │   ├── UserStatus.java
│   │   │                   │   └── UserType.java
│   │   │                   ├── controller/
│   │   │                   │   ├── UserController.java
│   │   │                   │   ├── PostController.java
│   │   │                   │   └── JobController.java
│   │   │                   ├── service/
│   │   │                   │   ├── UserService.java
│   │   │                   │   ├── PostService.java
│   │   │                   │   └── JobService.java
│   │   │                   ├── repository/
│   │   │                   │   ├── UserRepository.java
│   │   │                   │   ├── PostRepository.java
│   │   │                   │   └── JobRepository.java
│   │   │                   ├── dto/
│   │   │                   │   ├── UserDTO.java
│   │   │                   │   ├── PostDTO.java
│   │   │                   │   └── JobDTO.java
│   │   │                   ├── config/
│   │   │                   │   ├── SecurityConfig.java
│   │   │                   │   ├── WebConfig.java
│   │   │                   │   ├── AppConfig.java
│   │   │                   │   └── DatabaseConnectionChecker.java
│   │   │                   ├── security/
│   │   │                   │   ├── JwtAuthenticationFilter.java
│   │   │                   │   ├── JwtTokenProvider.java
│   │   │                   │   └── UserDetailsServiceImpl.java
│   │   │                   ├── exception/
│   │   │                   │   ├── GlobalExceptionHandler.java
│   │   │                   │   ├── ResourceNotFoundException.java
│   │   │                   │   └── CustomException.java
│   │   │                   ├── util/
│   │   │                   │   ├── DateUtils.java
│   │   │                   │   └── StringUtils.java
│   │   │                   └── ActsConnectBackendApplication.java
│   │   ├── resources/
│   │   │   ├── application.properties
│   │   │   ├── data.sql
│   │   │   ├── schema.sql
│   │   │   ├── static/
│   │   │   │   ├── css/
│   │   │   │   └── js/
│   │   │   └── templates/
│   │   │       └── index.html
│   └── test/
│       └── java/
│           └── com/
│               └── connect/
│                   └── acts/
│                       └── ActsConnectBackend/
│                           ├── UserServiceTest.java
│                           ├── PostServiceTest.java
│                           └── JobServiceTest.java
├── pom.xml
└── README.md
