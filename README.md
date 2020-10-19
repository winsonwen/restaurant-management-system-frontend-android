# restaurant-management-system-frontend-android
Compelete sign in and sign up

# Overall ---  Restaurant Management System

Restaurant Management System is a duo platform software that can run on both Windows and the Android phone. For Windows platform, this system is mainly designed to help restaurant managers to check the inventory, arrange employee shifts, and handle customer’s questions or complaints. For Android, the app is used to check the delivery status of the customer’s order in real time. Both platforms have the same functionalities, but it is more commonly used by each group of people, it is not limited. Customers can use Windows versions of software to check their delivery orders, and the restaurant staff can use Android phones to perform the same daily tasks as needed.

This project is a two way communication between server and the software. Those two platforms are isolated. They are not directly connected but they are indirectly connected through a server. Due to the structure of this design, both platforms can synchronize the data in real time. Such as the delivery checking system, employee management system, and reservation system etc. Each functionality is running as an independent instance to interact with the server and update the data in the server to perform the real time transactions.

The server has a database running and with port forwarding technology. Each port  is open for access to the database from Android app or Windows software. So, there won’t be any interaction from software and the android app directly. The database is designed to store and update the data only. So the server is going to handle only the http request from both Android app and Windows software. 
Both Android app and Windows software are the access point to the database by port forwarding as well. Users don't need to join the same network to access the database due to the port forwarding technology. There is no authentication needed by the user, everything is preconfigured. Because of the nature of the structure, both software and application are only going to send the http request to the server and wait for the server's response. So, the software or application will display the information that is sent back from the server.


## Version Note:

 V0.1

Recommanded device

1.Emulator from Android Studio(API level 29 or above)

2.Android device with version 10 or higher

For the first demo, we finished the first version of sign in and sign up form. Now, users can register and log in using our software.






