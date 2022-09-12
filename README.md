# Project Plan
***
## Mastery week project - Don't Wreck My House

## Tasks
### Set up and structure project - 15 minutes
### Add Spring DI
### Implement the Data Layer
#### Create the View Reservations for Host feature
##### Create Reservation File Repository
* [ ] Set up a unique identifier (email address) for host and guests - time estimate: 30 minutes
* [ ] Set up an id to track reservations - time estimate: 30 minutes
* [ ] Add CRUD functionality - time estimate: 1.5 hours
  * Create a reservation - add()
  * Read a reservation - findAll(), findById()
  * Update a reservation - update()
  * Delete a reservation - deleteById()
* [ ] Create Methods - 1 hour
  * findById - host email address
  * writeToFile
  * serialize
  * deserialize
* [ ] Add unit tests - 2 hours
  
##### Create a Reservation Repository
##### Define the interface
* findAll()
* add()
* findByDate()
* findById()
* deleteById()

##### Create Data Exception

### Implement the Domain Layer
#### Create Reservation Service
* [ ] Add CRUD functionality - time estimate: 1 hour
  * Create a reservation - add()
  * Read a reservation - findAll(), findById()
  * Update a reservation - update()
  * Delete a reservation - deleteById()
* [ ] Create Methods - 2 hours
  * validate() - 30 minutes
    (Validation must be implemented to ensure that dates do not overlap)
    * Existing reservation -
      - if a date ends in the middle of an existing reservation,
        the reservation is not valid and cannot be booked
      - if a date begins in the middle of an existing reservation,
        the reservation is not valid and cannot be booked
      - Start and end dates are required

#### Create Reservation Result
* [ ] Create Methods - 2 hours
  * getReservation()
  * setReservation()
  * messages arraylist
  * addMessage()
  * isSuccess() - used to confirm that the reservation (dates, total) is correct
* [ ] Add unit tests - 2 hours

### Create Models
* Menu enum - 10 minutes
  * Exit
  * View Reservations for Host
  * Make a Reservation
  * Edit a Reservation
  * Cancel a Reservation
* Reservation.java - 1 hour
  * Define fields
    * int id
    * LocalDate date
    * String guestName
    * String guestEmail
  * Getters and setters for each
  * toString method

### Implement the UI
## Create the Controller
* [ ] Add the menuOption
* [ ] Create Methods - 2 hours
  * addReservation()
  * viewReservation() - use host email address
  * updateReservation()
  * deleteReservation()

## Create the menu
* Menu enum

## Create the view
* [ ] Implement a scanner
* [ ] Create Methods - 3 hours
  * displayMenu() 
  * makeReservation()
  * updateById()
  * displayHeader()
  * readString()
  * readRequiredString()
  * displayResult()
  * displayText()
  * displayErrors()
  * readSection()
  * readInt()

## Schedule
### Tuesday
* Create Data Layer
  * Create Reservation File Repository
  * Create Reservation Repository
  * Unit tests

### Wednesday
* Create Domain Layer
  * Reservation Service
  * Reservation Result

### Thursday
* Unit tests for domain layer
* Create UI Layer (if time permits)
  * Controller
  * Menu
  * View

## Friday
* Continue working on the UI layer

## Weekend
* Continue working on the UI layer
* Work out remaining project bugs

## Approach
I will approach this from back to front starting with the data layer
and working my way to the ui layer.
