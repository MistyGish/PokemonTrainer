package trainer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		// Game Menu
		System.out.println("----- Main Menu -----");
		System.out.println(" ");
		System.out.println("1. Explore");
		System.out.println("2. List Pokemon");
		System.out.println("3. Set Fighter");
		System.out.println("4. Release a Pokemon");
		System.out.println("5. Exit");
		System.out.println(" ");

		// ArrayList
		ArrayList<Pokemon> ownedPokemon = new ArrayList<>();

		loadTeam(ownedPokemon);

		// Loop to allow the user to choose from the menu
		while (true) {
			// Get the user choice
			System.out.print("What would you like to do? Enter 1-5: ");
			Scanner userChoice = new Scanner(System.in);
			int menuChoice = userChoice.nextInt();
			userChoice.nextLine();

			// Respond to user choice
			if (menuChoice == 1) {
				explore(userChoice, ownedPokemon);
			}

			else if (menuChoice == 2) {
				listTeam(ownedPokemon);
			}

			else if (menuChoice == 3) {
				setFighter(ownedPokemon, userChoice);
			}

			else if (menuChoice == 4) {
				releasePokemon(ownedPokemon, userChoice);
			}

			else if (menuChoice == 5) {
				System.out.print("Exiting Progam...");
				break;
			}

			else {
				System.out.println("Input was invalid. Enter 1-4.");
			}
		}
	}

	// Assign first Pokemon in team at random to ArrayList
	public static void firstRun(ArrayList<Pokemon> owned) {
		Pokemon firstPokemon = new Pokemon();
		firstPokemon.type = PokemonType.getRandom();
		String type = firstPokemon.type.toString();
		firstPokemon.level = 1;
		firstPokemon.fighter = true;

		Connection connection = getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement("INSERT INTO pokemon (type, level, fighter) VALUES (?, ?, ?)");
			stmt.setString(1, type);
			stmt.setInt(2, firstPokemon.level);
			stmt.setBoolean(3, firstPokemon.fighter);
			stmt.executeUpdate();

		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		owned.add(firstPokemon);
	}

	// Show team of Pokemon currently owned
	public static void listTeam(ArrayList<Pokemon> owned) {
		System.out.println("Here is your team!");
		for (int i = 0; i < owned.size(); i++) {
			System.out.println((i + 1) + ". " + owned.get(i).getPokemonInfo());
		}
	}

	// Allows the chance to explore and possibly encounter wild Pokemon
	public static void explore(Scanner input, ArrayList<Pokemon> owned) {
		Random random = new Random();
		boolean wildPokemon = random.nextBoolean();
		int min = 1;
		int max = 5;

		if (wildPokemon) {
			Pokemon enemy = new Pokemon();
			enemy.type = PokemonType.getRandom();
			enemy.level = random.nextInt(max - min + 1) + min;

			System.out.println("A wild " + enemy.type + ", level " + enemy.level + " has appeared!");
			System.out.println("What would you like to do?");
			System.out.println("1: Fight!");
			System.out.println("2. Capture.");
			int choice = input.nextInt();
			input.nextLine();

			if (choice == 1) {
				fight(owned, max, enemy);
			}

			else if (choice == 2) {
				capture(owned, enemy);
			}
		}

		else {
			System.out.println("Nothing happened...");
		}

	}

	// Function for if user picks fight, Find user's Fighter Pokemon and compare to
	// the enemy
	// Add level to Fighter Pokemon if it beats the enemy
	public static void fight(ArrayList<Pokemon> owned, int maximum, Pokemon randEnemy) {
		for (Pokemon pokemon : owned) {
			if (pokemon.fighter) {
				if (pokemon.getAttackValue() >= randEnemy.getAttackValue()) {
					System.out.println("You won!");
					if (pokemon.level < maximum) {
						pokemon.level++;
					} else {
						System.out.println(pokemon.type + " is at the max level!");
					}

				} else {
					System.out.println("You lost...");
				}
			}
		}
	}

	// Find user's Fighter Pokemon and compare to the enemy
	// Add Pokemon to ArrayList of owned Pokemon if it beats the enemy
	public static void capture(ArrayList<Pokemon> owned, Pokemon randEnemy) {
		for (int i = 0; i < owned.size(); i++) {
			if (owned.get(i).fighter) {
				if (owned.get(i).getAttackValue() >= randEnemy.getAttackValue()) {
					System.out.println("You got them!");
					owned.add(randEnemy);
				} else {
					System.out.println("They got away...");
				}
			}
		}
	}

	// Allow user to change their current fighter out of owned Pokemon
	public static void setFighter(ArrayList<Pokemon> owned, Scanner input) {
		System.out.println("Which Pokemon do you want to fight? Enter the index: ");

		// Change current fighter to FALSE
		for (int i = 0; i < owned.size(); i++) {
			if (owned.get(i).fighter) {
				owned.get(i).fighter = false;
			}
		}

		// Set user chosen fighter to TRUE
		int setFighter = input.nextInt();
		owned.get(setFighter - 1).fighter = true;

	}

	public static void releasePokemon(ArrayList<Pokemon> owned, Scanner input) {
		if (owned.size() == 1) {
			System.out.println("You only have one Pokemon on your team!");
		}

		else {
			System.out.println("Which Pokemon do you want to release? Enter the index: ");
			int removePokemon = input.nextInt();

			if (removePokemon > owned.size()) {
				System.out.println("That index doesn't exist.");
			}

			else if (owned.get(removePokemon - 1).fighter) {
				System.out.println("You can't release the fighter!");
			}

			else {
				owned.remove(removePokemon - 1);
			}
		}
	}

	public static void loadTeam(ArrayList<Pokemon> owned) {
		// create a new connection
		Connection connection = getConnection();
		System.out.println("connection: " + connection);

		Statement stmt = null; // Executing SQL statement called stmt
		ResultSet rs = null; // Database ResultSet from stmt execution called rs

		try {
			stmt = connection.createStatement();
			String query = "SELECT * FROM pokemon";
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				String type = rs.getString("type");
				int level = rs.getInt("level");
				boolean fighter = rs.getBoolean("fighter");

				Pokemon pokemon = new Pokemon();
				pokemon.type = PokemonType.valueOf(type);
				pokemon.level = level;
				pokemon.fighter = fighter;
				owned.add(pokemon);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		if (owned.isEmpty()) {
			// Assign first Pokemon
			firstRun(owned);
		} else {
			System.out.println();
		}
	}

	private static Connection getConnection() {
		try {
			String url = "jdbc:mysql://localhost:3306/pokemondb";
			String username = "root";
			String password = "123456";
			Connection connection = DriverManager.getConnection(url, username, password);
			return connection;
		} catch (SQLException e) {
			System.out.println(e);
		}
		return null;
	}

}
