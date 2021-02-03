# CLI Course Registration Service
#### System Programming course assignment.
#### Course registration service, allowing users to register as a student or as an admin and perform various tasks.

### Usage:
| Command | Summary | Notes |
| ------ | ------ | ------ |
| ```ADMINREG [username] [password]``` | Registers [username] as an admin with the password [password]. | - |
| ```STUDENTREG [username] [password]``` | Registers [username] as a student with the password [password]. | - |
| ```LOGIN [username] [password]``` | Login using [username] and [password]. | - |
| ```LOGOUT``` | Logout the user currently logged-in. | Must be logged-in. |
| ```KDAMCHECK [course_number]``` | Get a list of courses required to register for [course_number]. | Must be logged-in. |
| ```COURSEREG [course_number]``` | Register to [course_number]. | Student only. Must be logged-in. |
| ```STUDENTSTAT [username]``` | Get a list of courses [username] is registered to. | Admin only. Must be logged-in. |
| ```COURSESTAT [course_number]``` | Get the name, capacity, number of available seats, and registered students for [course_number]. | Admin only. Must be logged-in. |
| ```ISREGISTERED [course_number]``` | Check if the logged-in user is registered to [course_number]. | Student only. Must be logged-in. |
| ```UNREGISTER [course_number]``` | Unregister the logged-in student from [course_number]. | Student only. Must be logged-in. |
| ```MYCOURSES``` | Get a list of courses the logged-in user is registered to. | Student only. Must be logged-in. |
