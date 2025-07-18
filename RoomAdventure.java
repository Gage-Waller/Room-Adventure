import java.util.Scanner; // Import Scanner for reading user input

public class RoomAdventure { // Main class containing game logic

    // class variables
    private static Room currentRoom; // The room the player is currently in
    private static String[] inventory = {null, null, null, null, null, null, null}; // Player inventory slots
    private static String status; // Message to display after each action
    private static int score = 0; // Player score
    private static boolean hasCat; // check if you have cat


    // constants
    final private static String DEFAULT_STATUS =
        "Sorry, I do not understand. Try [verb] [noun]. Valid verbs include 'go', 'look', 'take', 'use', 'observe', 'pet', and 'eat'."; // Default error message



    private static void handleGo(String noun) { // Handles moving between rooms
        String[] exitDirections = currentRoom.getExitDirections(); // Get available directions
        Room[] exitDestinations = currentRoom.getExitDestinations(); // Get rooms in those directions
        status = "I don't see that room."; // Default if direction not found
        for (int i = 0; i < exitDirections.length; i++) { // Loop through directions
            if (noun.equals(exitDirections[i])) { // If user direction matches
                if (currentRoom.getName().equals("Room 4") && noun.equals("south")){
                    if(hasCat){
                        System.out.println("You escaped with your new best friend(the cat) You live happily ever after with your favorite companion");
                    } else{
                    System.out.println("You escaped!");
                    }
                    System.out.println("Final score: " + score);
                    System.exit(0); // End the game if in final room and going south
                }
                currentRoom = exitDestinations[i]; // Change current room
                score += 1; // +1 for entering a new room
                status = "Changed Room"; // Update status
                return; // Exit loop
            }
        }
    }

    private static void handleLook(String noun) { // Handles inspecting items
        String[] items = currentRoom.getItems(); // Visible items in current room
        String[] itemDescriptions = currentRoom.getItemDescriptions(); // Descriptions for each item
        status = "I don't see that item."; // Default if item not found
        for (int i = 0; i < items.length; i++) { // Loop through items
            if (noun.equals(items[i])) { // If user-noun matches an item
                status = itemDescriptions[i]; // Set status to item description
            }
        }
    }

    private static void handleTake(String noun) { // Handles picking up items
        String[] grabbables = currentRoom.getGrabbables(); // Items that can be taken
        status = "I can't grab that."; // Default if not grabbable
        for (String item : grabbables) { // Loop through grabbable items
            if (noun.equals(item)) { // If user-noun matches grabbable
                for (int j = 0; j < inventory.length; j++) { // Find empty inventory slot
                    if (inventory[j] == null) { // If slot is empty
                        inventory[j] = noun; // Add item to inventory
                        status = "Added it to the inventory"; // Update status
                        score += 10; // +10 for taking an item
                        break; // Exit inventory loop
                    }
                }
            }
        }
    }

    private static void handleUse(String noun) { // Handles using items

        // Checks if item is in inventory
        boolean hasItem = false;
        int itemIndex = -1;

        for (int i = 0; i < inventory.length; i++) {
            if (noun.equals(inventory[i])) {
                hasItem = true;
                itemIndex = i;
                break;
            }
        }

        // Player doesnt have item
        if (!hasItem) {
            status = "You don't have that item.";
            return;
        }

        // Handle item-specific use cases by room
        // switch is like an if-else statement 
        // but executes the chosen case "block" of code 
        // helps with easier structure
        switch (noun) {
            case "key":
                if (currentRoom.getName().equals("Room 4")) {
                    status = "You unlock the chest and find the treasure!";
                    // Can take treasure now
                    currentRoom.setGrabbables(new String[] {"treasure"});
                    inventory[itemIndex] = null; // Remove when used
                    score += 15; // +15 for using the key
                } else {
                    status = "You can't use the key here.";
                }
                break;

            case "coal":
                if (currentRoom.getName().equals("Room 2")) {
                    status = "You toss the coal into the fireplace. It lights on fire!";
                    inventory[itemIndex] = null; // Remove when used
                    score += 15; // +15 for using the coal
                } else {
                    status = "That won't help here.";
                }
                break;

            case "sword":
                if (currentRoom.getName().equals("Room 3")) {
                    status = "You swing the sword at the statue. It clangs back!";
                } else {
                    status = "You wave the sword around.";
                }
                break;

            case "cat_toy":
                if (currentRoom.getName().equals("Room 6")) {
                    status = "You play with the cat for a bit. It seems you have gained a new follower!";
                    hasCat = true;
                } else {
                    status = "You shake the cat toy and it lights up.";
                }
                break;

            default:
                status = "You can't use that here.";}
    }

    private static void handleHelp() {
            String output = String.format("Current Commands are, go, take, use, eat, help, look, and exit commands.\nThe Go command allows you to move in the 4 different directions which are north, south, east, west. To use the go command you input go (direction)\nThe take command allows you to take items from a room and add them to your inventory. To use the command do take (item name)\nThe look command allows you to examine an object in the room. To use the look command do look (object)\nTo Exit the game you can type either exit, leave, or quit");
            System.out.println(output);
    }

    private static void handleEat(String noun) {
    boolean hasItem = false;
    int itemIndex = -1;

    for (int i = 0; i < inventory.length; i++) {
        if (noun.equalsIgnoreCase(inventory[i])) {
            hasItem = true;
            itemIndex = i;
            break;
        }
    }

    if (!hasItem) {
        status = "You don't have that item to eat.";
        return;
    }

    if (noun.equalsIgnoreCase("energy_booster")) {
        inventory[itemIndex] = null; // Remove it from inventory
        status = "You ate the energy booster. You feel revitalized!";
        score += 5; // Give a small score bonus
    } else {
        status = "You can't eat that!";}
    }
    
private static void handleObserve(String noun) {
    boolean found = false;

    for (String item : inventory) {
        if (item != null && item.equalsIgnoreCase(noun)) {
            found = true;

            // Provide descriptions for known items
            switch (item) {
                case "key":
                    status = "It's a small brass key. It looks old.";
                    break;
                case "coal":
                    status = "A dusty lump of coal. Feels cold.";
                    break;
                case "sword":
                    status = "A heavy iron sword. Slightly rusted.";
                    break;
                case "energy_booster":
                    status = "A glowing bottle labeled 'Energy Booster'. Smells fruity.";
                    break;
                case "treasure":
                    status = "A pile of gold and jewels! This is what you came for!";
                    break;
                default:
                    status = "It's " + item + ", but you aren't sure what it does.";
                    break;
            }

            break; // No need to check further
        }
    }

    if (!found) {
        status = "You don't have that item in your inventory.";
    }
}


    private static void setupGame() { // Initializes game world
        Room room1 = new Room("Room 1"); // Create Room 1
        Room room2 = new Room("Room 2"); // Create Room 2
        Room room3 = new Room("Room 3"); // Create Room 3
        Room room4 = new Room("Room 4"); // Create Room 4
        Room room5 = new Room("Room 5"); // Create Room 5
        Room room6 = new Room("Room 6"); // Create Room 6

        String[] room1ExitDirections = {"east"}; // Room 1 exits
        Room[] room1ExitDestinations = {room2}; // Destination rooms for Room 1
        String[] room1Items = {"chair", "desk"}; // Items in Room 1
        String[] room1ItemDescriptions = { // Descriptions for Room 1 items
            "It is a chair",
            "It's a desk, there is a key on it."
        };
        String[] room1Grabbables = {"key"}; // Items you can take in Room 1
        room1.setExitDirections(room1ExitDirections); // Set exits
        room1.setExitDestinations(room1ExitDestinations); // Set exit destinations
        room1.setItems(room1Items); // Set visible items
        room1.setItemDescriptions(room1ItemDescriptions); // Set item descriptions
        room1.setGrabbables(room1Grabbables); // Set grabbable items

        String[] room2ExitDirections = {"west", "south", "east"}; // Room 2 exits
        Room[] room2ExitDestinations = {room1, room3, room6}; // Destination rooms for Room 2
        String[] room2Items = {"fireplace", "rug", "energy_booster"}; // Items in Room 2
        String[] room2ItemDescriptions = { // Descriptions for Room 2 items
            "It's on fire",
            "There is a lump of coal on the rug.",
            "It's a glowing bottle labeled 'Energy Booster'. You feel energized just looking at it."
        };
        String[] room2Grabbables = {"coal", "energy_booster"}; // Items you can take in Room 2
        room2.setExitDirections(room2ExitDirections); // Set exits
        room2.setExitDestinations(room2ExitDestinations); // Set exit destinations
        room2.setItems(room2Items); // Set visible items
        room2.setItemDescriptions(room2ItemDescriptions); // Set item descriptions
        room2.setGrabbables(room2Grabbables); // Set grabbable items

        String[] room3ExitDirections = {"north", "west"}; // Room 3 exits
        Room[] room3ExitDestinations = {room2, room4}; // Destination rooms for Room 3
        String[] room3Items = {"painting", "statue"}; // Items in Room 3
        String[] room3ItemDescriptions = { // Descriptions for Room 3 items
            "It's a beautiful painting.",
            "It's a tall statue, it looks like a knight and has a sword."
        };
        String[] room3Grabbables = {"sword"}; // Items you can take in Room 3
        room3.setExitDirections(room3ExitDirections); // Set exits
        room3.setExitDestinations(room3ExitDestinations); // Set exit destinations
        room3.setItems(room3Items); // Set visible items
        room3.setItemDescriptions(room3ItemDescriptions); // Set item descriptions
        room3.setGrabbables(room3Grabbables); // Set grabbable items

        String[] room4ExitDirections = {"south", "east", "north"}; // Room 4 exits
        Room[] room4ExitDestinations = {null, room3, room5}; // Destination rooms for Room 4
        String[] room4Items = {"chest", "energy_booster"}; // Items in Room 4
        String[] room4ItemDescriptions = { // Descriptions for Room 4 items
            "It's a large treasure chest with treasure inside.",
            "Another bottle of Energy Booster. Looks like someone left it here!"
        };
        String[] room4Grabbables = {"treasure", "energy_booster"}; // Items you can take in Room 4
        room4.setExitDirections(room4ExitDirections); // Set exits
        room4.setExitDestinations(room4ExitDestinations); // Set exit destinations
        room4.setItems(room4Items); // Set visible items
        room4.setItemDescriptions(room4ItemDescriptions); // Set item descriptions
        room4.setGrabbables(room4Grabbables); // Set grabbable items

        String[] room5ExitDirections = {"south"}; // Room 5 exits
        Room[] room5ExitDestinations = {room4}; // Destination rooms for Room 5
        String[] room5Items = {"chandelier", "paper"}; // Items in Room 5
        String[] room5ItemDescriptions = { // Descriptions for Room 5 items
            "A beautiful glass chandelier hangs from the ceiling. A cat toy seems to be stuck inside of it.",
            "The paper says 'beware of the black cat'."
        };
        String[] room5Grabbables = {"cat_toy"}; // Items you can take in Room 5
        room5.setExitDirections(room5ExitDirections); // Set exits
        room5.setExitDestinations(room5ExitDestinations); // Set exit destinations
        room5.setItems(room5Items); // Set visible items
        room5.setItemDescriptions(room5ItemDescriptions); // Set item descriptions
        room5.setGrabbables(room5Grabbables); // Set grabbable items

        String[] room6ExitDirections = {"west"}; // Room 6 exits
        Room[] room6ExitDestinations = {room2}; // Destination rooms for Room 6
        String[] room6Items = {"Cat", "cat_bed"}; // Items in Room 6
        String[] room6ItemDescriptions = { // Descriptions for Room 6 items
            "A black cat stands before you. It's tail waves slowly back and forth. It seems cautious of you."
            , "A bed that the black cat presumebally sleeps on. It looks quite soft."
        };
        String[] room6Grabbables = {}; // Items you can take in Room 4
        room6.setExitDirections(room6ExitDirections); // Set exits
        room6.setExitDestinations(room6ExitDestinations); // Set exit destinations
        room6.setItems(room6Items); // Set visible items
        room6.setItemDescriptions(room6ItemDescriptions); // Set item descriptions
        room6.setGrabbables(room6Grabbables); // Set grabbable items

        currentRoom = room1; // Start game in Room 1
    }
    
    @SuppressWarnings("resource")
    public static void main(String[] args) { // Entry point of the program
        setupGame(); // Initialize rooms, items, and starting room

        while (true) { // Game loop, runs until program is terminated
            System.out.print(currentRoom.toString()); // Display current room description
            System.out.print("Inventory: "); // Prompt for inventory display

            for (int i = 0; i < inventory.length; i++) { // Loop through inventory slots
                System.out.print(inventory[i] + " "); // Print each inventory item
            }
            
            System.out.println("\nScore: " + score); // Display current score

            System.out.println("\nWhat would you like to do? "); // Prompt user for next action

            Scanner s = new Scanner(System.in); // Create Scanner to read input
            String input = s.nextLine(); // Read entire line of input
            String[] words = input.split(" "); // Split input into words

            if (input.equalsIgnoreCase("help")) {
                handleHelp();
                continue;
            }

            if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("leave")){
                System.out.println("Thanks for playing!");
                System.exit(0);
            }

            if (words.length != 2) { // Check for proper two-word command
                status = DEFAULT_STATUS; // Set status to error message
                continue; // Skip to next loop iteration
            }

            String verb = words[0]; // First word is the action verb
            String noun = words[1]; // Second word is the target noun


            if (verb.equalsIgnoreCase("go")) {
                handleGo(noun);
            } else if (verb.equalsIgnoreCase("look")) {
                handleLook(noun);
            } else if (verb.equalsIgnoreCase("take")) {
                handleTake(noun);
            } else if (verb.equalsIgnoreCase("use")) {
                handleUse(noun);
            } else if (verb.equalsIgnoreCase("eat")) {
                handleEat(noun);
            } else if (verb.equalsIgnoreCase("observe")) {
                handleObserve(noun);        
            } else {
                status = DEFAULT_STATUS;
            }
            System.out.println(status); // Print the status message
        }
    }
}

class Room { // Represents a game room
    private String name; // Room name
    private String[] exitDirections; // Directions you can go
    private Room[] exitDestinations; // Rooms reached by each direction
    private String[] items; // Items visible in the room
    private String[] itemDescriptions; // Descriptions for those items
    private String[] grabbables; // Items you can take

    public Room(String name) { // Constructor
        this.name = name; // Set the room's name
    }

    public void setExitDirections(String[] exitDirections) { // Setter for exits
        this.exitDirections = exitDirections;
    }

    public String[] getExitDirections() { // Getter for exits
        return exitDirections;
    }

    public void setExitDestinations(Room[] exitDestinations) { // Setter for exit destinations
        this.exitDestinations = exitDestinations;
    }

    public Room[] getExitDestinations() { // Getter for exit destinations
        return exitDestinations;
    }

    public void setItems(String[] items) { // Setter for items
        this.items = items;
    }

    public String[] getItems() { // Getter for items
        return items;
    }

    public void setItemDescriptions(String[] itemDescriptions) { // Setter for descriptions
        this.itemDescriptions = itemDescriptions;
    }

    public String[] getItemDescriptions() { // Getter for descriptions
        return itemDescriptions;
    }

    public void setGrabbables(String[] grabbables) { // Setter for grabbable items
        this.grabbables = grabbables;
    }

    public String[] getGrabbables() { // Getter for grabbable items
        return grabbables;
    }

    public String getName() { // Getter for room name
        return name;
    }

    public void setName(String name) { // Setter for room name
        this.name = name;
    }

    @Override
    public String toString() { // Custom print for the room
        String result = "\nLocation: " + name; // Show room name
        result += "\nYou See: "; // List items
        for (String item : items) { // Loop items
            result += item + " "; // Append each item
        }
        result += "\nExits: "; // List exits
        for (String direction : exitDirections) { // Loop exits
            result += direction + " "; // Append each direction
        }
        return result + "\n"; // Return full description
    }
}