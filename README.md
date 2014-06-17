Ten!
===
Assessment Java Programming

Theme
===
Goal of this assignment: getting a ten! Loosely based on some popular games you can currently find on the internet and as app for iOS and Android.

Gameplay
===
A 4x4 board will display numbers between 0 and 10. By choosing a direction (North/South/East/West) the player moves the numbers in all rows (East/West) or columns (North/South) which causes two equal numbers to melt into the next number. For example: two numbers 4 melt together into one number 5. This may happen a maximum of two times per row or column.
A new number 0 is placed randomly at the beginning of one of the changed rows or columns. If nothing has changed in any of the 4 lines, no number 0 will be added.
The game starts with a board containing two numbers 0 at randomly picked locations. The player wins as soon as the number 10 appears on the board. If no more moves are possible, the final score is the highest number on the board with the second highest number as decimal; this may be the same number. The player will be notified of this.

Structure
===
At the start of the game a windows is displayed. This window will remain visible until closed in the common way on the used platform (e.g. on a Mac by clicking the red circle at the top left-hand corner). The window is divided into two panels: the board at the top, the control panel below. The window has a fixed width, precisely enough to fit the menu bar, the board and the control panel.

Board
===
The board exists out of 4x4 containers of 100x100 pixels. The centre of each container will have a number between 0 and 10, unless that container is empty.

Control panel
===
The control panel contains four buttons representing the four directions and the current score in the centre. The user may also use cursor keys to choose a direction.


File menu
===
There's a menu bar with a single menu named "File". This menu has two items. The "Store" item makes sure that the current state of the board is saved in a file selected by the user as four lines of four numbers separated by spaces. An empty field is represented by the value -1. The "Load" item makes sure that an already saved state can be read and loaded.
Both actions require using their default dialogue in Swing.

Technical requirements
===
* The application uses the principles of Object Oriented Programming.
* The application is structured according to the MVC architecture.
* For its GUI the application uses the Java Swing-library.
* Names of classes, methods, variables and other stuff in the program code have to be in English.
