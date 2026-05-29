Banking Management System

A console-based Banking Management System developed in Java using layered architecture, SOLID principles, and multiple object-oriented design patterns. The system simulates core banking operations such as account management, authentication, card services, loan processing, and role-based banking operations for customers, employees, and managers.

Features
Customer Functionalities
Customer registration and login
Create checking accounts
Create deposit accounts
Create currency accounts
Create debit cards
Create credit cards
View account details
View account statistics
View transaction information
Request loans
Repay loans
View credit scores
Password validation and secure login
Account freezing after failed login attempts
Employee Functionalities
Employee login
View customer account details
View customer credit scores
Process customer loan requests
Repay customer loans
Manager Functionalities
Manager login
Create employee accounts
View customer account information
Process customer banking operations
Manage customer financial activities
System Architecture

The project follows a layered architecture:

Presentation Layer
        ↓
Application Layer
        ↓
Infrastructure Layer
        ↓
File Storage
Project Structure
banking/
│
├── application/
│   ├── BankFacade.java
│   ├── LoanManager.java
│   ├── CreditScoreManager.java
│   └── ...
│
├── domain/
│   ├── accounts/
│   ├── cards/
│   ├── loans/
│   └── users/
│
├── infrastructure/
│   ├── FileAccountRepository.java
│   ├── FileUserRepository.java
│   ├── CardRepo.java
│   ├── LoanRepo.java
│   ├── SecurityUtil.java
│   └── FileHandler.java
│
├── presentation/
│   ├── Login.java
│   ├── menu/
│   └── utils/
│
└── data/
    ├── users/
    ├── accounts/
    ├── cards/
    └── loans/

Technologies Used
Java
Object-Oriented Programming (OOP)
File-Based Persistence
Java Collections Framework
Java NIO File Handling
SHA Hashing for Password Security
Design Patterns Implemented
Design Pattern	Purpose
Facade Pattern	Centralized application access
Strategy Pattern	Role-based menu behaviors
Factory Pattern	User and account object creation
Repository Pattern	Data persistence abstraction
Singleton Pattern	Centralized file management
Dependency Injection	Loose coupling between components
SOLID Principles Applied
Single Responsibility Principle (SRP)
Open/Closed Principle (OCP)
Liskov Substitution Principle (LSP)
Interface Segregation Principle (ISP)
Dependency Inversion Principle (DIP)
Account Types
Checking Account
Used for daily banking activities
Supports debit cards
Supports loan deposits
Deposit Account
Fixed-term savings account
Maturity-based deposits
Currency Account
Foreign currency support
Supports USD and EUR balances
Loan System

The banking system includes a loan management subsystem with:

Loan requests
Loan repayment
Credit score evaluation
Loan eligibility validation
Interest rate calculation

Credit scores are calculated using:

Credit card utilization
Existing debt
Active loans
Repaid loan history
Security Features
Password hashing
Login validation
Role-based access control
Failed login attempt protection
Account freezing system
File-Based Persistence

The system stores data using structured text files.

Example User File
4001#John#Doe#hashedPassword
Example Account File
Checking#1001#15000.0#true#833988546
Deposit#2001#5000.0#2028-04-27
Currency#3001#2500.0#USD
ID Generation System

The system automatically generates unique IDs:

Entity	Starting Range
Checking Accounts	1000+
Deposit Accounts	2000+
Currency Accounts	3000+
Customers	4000+
Employees	5000+
Managers	6000+
How to Run the Project
Clone the Repository
git clone https://github.com/yerkam/example_bank_system.git
Open the Project

Import the project into:

Eclipse
IntelliJ IDEA
VS Code
Run the Application

Run:

Login.java
Sample Login Flow
1. CUSTOMER LOGIN
2. EMPLOYEE LOGIN
3. MANAGER LOGIN

Sample Customer Menu
CC   -> Create Checking Account
CD   -> Create Deposit Account
CCU  -> Create Currency Account
CCA  -> Create Credit Card
DCA  -> Create Debit Card
VA   -> View Account Details
VT   -> View Transactions
TL   -> Take Loan
RSP  -> Reset Password
EXIT -> Exit
Example Functionalities
Create Checking Account
bankFacade.createCheckingAccount(
    firstName,
    lastName,
    userId,
    password,
    balance,
    true
);
Request Loan
bankFacade.requestLoan(
    userId,
    checkingAccountNumber,
    amount,
    months
);


Future Improvements
Possible future enhancements include:
Database integration (MySQL/PostgreSQL)
Graphical User Interface (GUI)
Online banking API integration
Transaction history tracking
Multi-currency conversion
Advanced loan approval systems
Unit and integration testing
Spring Boot migration
REST API implementation
Educational Purpose

This project was developed as an educational software engineering and object-oriented programming project to demonstrate:
layered architecture,
design patterns,
SOLID principles,
repository abstraction,
and banking domain modeling.


License

This project is open-source and available under the MIT License.
