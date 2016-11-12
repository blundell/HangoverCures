A work in progress to update this app I wrote in 2010 and opensource it

___

Outer circles are mechanisms
Inner circles are policies

Source code dependencies can only point inwards,
 inner circles cannot know about outer circles
 data formats used in outer circles should not be used by inner circles

Entities encapsulate enterprise wide business rules
 they can be objects and methods or just data and functions
 encapsulate general and high level rules

Use cases contain application specific business rules
 encapsulate and implements all the use cases of the system
 orchestrate the flow of data to and from the entities
 direct the entities to use the enterprise wide business rules to achieve the use case

Interface adapters convert data most convenient for uses cases & entities
to the format most useful for external agencies such as a database or Android
 should wholly contain the MVC architecture of the GUI
 models are just data structures passed from the controllers to the use cases and back

Framework & drivers is composed of frameworks and tools such as the Database, Android, etc
 only glue code in this layer
 this layer is where all the implementation details go (Android is a detail)

