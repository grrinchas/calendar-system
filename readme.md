# Calendar System

Application allows users to manage their day-to-day schedule by allowing them to create, delete and modify
a meeting (event). 

It is written in Java using RMI and Swing technologies and is composed of three parts:

1. `Server` - contains logic for dealing with authentication, authorisation and database (Sqlite).
2. `Client` - is a swing application which uses RMI to communicate with the `Server`. Because
**Calendar System** is a distributive application, many clients can connect to the server. The change
in one client will be visible in another.
3. `Common` - defines API for communication between `Server` and a `Client`.

**NOTE:** Application was university assignment and was not designed for commercial or other usage. 
But, that does not mean that it can't be extended for that.

## Authentication

At the moment only predefined users are allowed to login. In other words, registration of the user 
is not implemented yet. Screen below shows initial login dialog, which is opened when application
is started.

![Login screen](http://i.imgur.com/c1tGQsv.png)

## Calendar views

At the moment users can see their meetings in two views: 

1. Month view - which is default view, shows very familiar month view of the calendar.
If clicked on the empty space within specific user will create a meeting. If clicked
on the meeting label, user will have ability to delete or modify it.

<img src="http://i.imgur.com/D6SQxct.png" height="500" width="700" >

2. Year view - shows months by the year. If clicked on the uncolored
day, user will create a meeting, while clicking on colored day will opened
modification dialog. 

<img src="http://i.imgur.com/srnailO.png" height="500" width="700" >

**NOTE:** at the moment there is no way to access all the meetings in the same day 
in the year view, only first meeting is displayed. To access other meetings,
 user should switch the view.

## CRUD operations

User can create and modify meetings by clicking on appropriate space within calendar views. 
At the moment meeting has these fields:

- Title (required)
- Location (optional)
- Start and end time (required)
- Description (optional)
- List of labels (optional)

<img src="http://i.imgur.com/VYTcgL9.png" height="500" width="700" >

**NOTE:** At the moment there are no user input checking implemented, thus user can enter
malicious text or choose start time after end time.

## Organising meetings

To have better organization of the meetings user can create, delete and update labels. Later he has a choice
to assign them to the meetings. Basically, labels work similarly to GitHub labels. A nice thing about them 
is ability for the user to toggle meetings based on them. For example, if clicked on the label which
is not active, user will show all meetings with that label. 

**NOTE:** At the moment, there are no way to hide meetings which does not have a label.

![Imgur](http://i.imgur.com/lAgc0xw.png)


## Further extensions

There are many ways how **Calendar System** can be extended. For example:
 
 - Add ability to share a meeting between different users. This of course would required to implement 
 notifications, access control etc.
 - Add ability to save meetings locally as json, xml or other types and later display them.
 - Add integration with existing calendar systems: google, facebook etc.
 
## Licence 

This project is licensed under the GNU General Public License v3.0 - see the 
[LICENSE.txt](https://github.com/grrinchas/calendar-system/blob/master/LICENSE.txt) file for details.

## Acknowledgments

Many thanks to [David Avery](https://github.com/DavidAveryUoB)
and [Peter McNeil](https://github.com/petermcneil) for their contribution.
