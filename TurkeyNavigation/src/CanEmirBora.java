/**
 * Turkey Navigation
 *
 * @author Can Emir Bora
 * @since Date: 04.04.2024
 */
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
public class CanEmirBora {
    public static void main(String[] args) throws FileNotFoundException {

        ArrayList<City> cities = new ArrayList<>(); //creating the cities ArrayList
        Scanner sc = new Scanner(new FileInputStream("city_coordinates.txt")); //scanning 'city_coordinates.txt' file
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] lineSplit = line.split(", "); //splitting each line
            cities.add(new City(lineSplit[0], Integer.parseInt(lineSplit[1]), Integer.parseInt(lineSplit[2]))); //adding each City according to their name and X,Y coordinates
        }
        sc.close(); //closing the scanner

        Scanner input = new Scanner(System.in); //setting the scanner input
        boolean isStartingNotTaken = true; //check values in order to control invalid city names
        boolean isDestinationNotTaken = true;
        String startingCity = null; // initializing the input cities
        String destinationCity = null;
        while (isStartingNotTaken) {
            System.out.print("Enter starting city: ");
            String s= input.next();
            String startingCity_ = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();//getting the starting city's name input
            for (City c : cities) {
                if (Objects.equals(c.getCityName(), startingCity_)) { // checks whether input city exists
                    startingCity = startingCity_; // getting the correct city name
                    isStartingNotTaken = false;
                    break;
                }
            }
            if (isStartingNotTaken) { //asking the user to enter a city name again
                System.out.println("City named " + "'" + startingCity_ + "'" + " not found. Please enter a valid city name.");
            }
        }
        while (isDestinationNotTaken) {
            System.out.print("Enter destination city: ");
            String s= input.next();
            String destinationCity_ = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();//getting the starting city's name input
            for (City c : cities) {
                if (Objects.equals(c.getCityName(), destinationCity_)) { // checks whether input city exists
                    destinationCity = destinationCity_; // getting the correct city name
                    isDestinationNotTaken = false;
                    break;
                }
            }
            if (isDestinationNotTaken) { //asking the user to enter a city name again
                System.out.println("City named " + "'" + destinationCity_ + "'" + " not found. Please enter a valid city name.");
            }
        }
        input.close();//closing the scanner

        //In my project,I used Dijkstra Algorithm which finds the shortest paths from a single starting point to all other points.In our projects point are called 'City'
        //In a nutshell, Dijkstra works by exploring neighbor cities and keeping the shortest distance discovered so far, until all cities are checked.
        ArrayList<City> allCities = new ArrayList<>(); // creating the ArrayList that I will use in multiple codes
        ArrayList<City> checkedCities = new ArrayList<>(); // creating the ArrayList to check whether cities are explored or not
        City emptyCity=new City("",-1,-1); // creating a non-useful empty city to use it in at start
        City previousCity=new City(0, "", emptyCity); // initializing the previous city with starting cities previous city which does not exist
        City currentCity=new City(0, startingCity, emptyCity); // initializing the current city
        ArrayList<String> adjacent = adjacentCities(currentCity.getCityName(), previousCity.getCityName()); // creating ArrayList for starting cities adjacent
        Scanner ct = new Scanner(new FileInputStream("city_coordinates.txt")); //scanning 'city_coordinates.txt' file
        while (ct.hasNextLine()) {
            String line = ct.nextLine();
            String[] lineSplit = line.split(", "); //splitting each line
            if (adjacent.contains(lineSplit[0])) { //checking if the city is one of the adjacent ones
                allCities.add(new City(distanceBetween(currentCity.getCityName(), lineSplit[0]), lineSplit[0], currentCity)); //adding adjacent ones to the allCities in other form of City which has parent value
            }
            else { // if it is not an adjacent
                allCities.add(new City(Double.MAX_VALUE, lineSplit[0], emptyCity));//adding other ones to the allCities in other form of City which has parent value
            }
        }
        ct.close();// closing the scanner
        int allCitiesLength = allCities.size();// taking the length of allCities ArrayList as a constant
        while (checkedCities.size() < allCitiesLength) { // checking that every city is checked in allCities
            previousCity = currentCity; //updating the previous city to the current city
            double minValue = Double.MAX_VALUE;
            for (City c : allCities) { //updating the current city to the city which has minimum distance value
                if (c.getDistanceValue() < minValue) {
                    currentCity = c;
                    minValue = c.getDistanceValue();
                }
            }
            ArrayList<String> adj = adjacentCities(currentCity.getCityName(), previousCity.getCityName()); // taking the current city's adjacent cities
            ArrayList<City> adjInCities = new ArrayList<>();
            for (City i : allCities) {// changing the form of adjacent cities from String to City
                if (adj.contains(i.getCityName())) {
                    adjInCities.add(i);
                }
            }
            for (City i : adjInCities) { // for every element in adjacent Cities
                if ((distanceBetween(currentCity.getCityName(), i.getCityName()) + currentCity.getDistanceValue()) < i.getDistanceValue()) { //deciding which path is shorter
                    i.setDistanceValue((distanceBetween(currentCity.getCityName(), i.getCityName()) + currentCity.getDistanceValue()));
                    i.setParent(currentCity);//changing the current cities parent
                }
            }
            checkedCities.add(currentCity); //adding the current city to the checkedCities in order to make the while loop work correctly
            allCities.remove(currentCity); // removing the current city from the allCities in order to make the while loop work correctly
        }

        ArrayList<City> finalCheckedCities = new ArrayList<>();
        for (City i: checkedCities){ // taking the every city in checkedCities only one time and add to finalCheckedCities
            if (!finalCheckedCities.remove(i)){
                finalCheckedCities.add(i);
            }
        }
        ArrayList<String> finalCheckedCitiesStr = new ArrayList<>();
        for (City i:finalCheckedCities){ //creating the same ArrayList but now with Strings. 
            finalCheckedCitiesStr.add(i.getCityName());
        }
        ArrayList<String> allCitiesStr = new ArrayList<>(); //copy of the cities

        for (City i:cities){//copying the cities
            allCitiesStr.add(i.getCityName());
        }
        ArrayList<City> allCitiesWithParents = new ArrayList<>();
        ArrayList<City> unreachableCities= new ArrayList<>();
        ArrayList<String> unreachableCitiesInStr= new ArrayList<>();
        for (String i: allCitiesStr){// checking all the cities whether they have connections with starting city
            if (finalCheckedCitiesStr.contains(i)){ 
                continue;
            }
            else {
                if (!Objects.equals(i,startingCity)){ // if they don't have connections, adding them into unreachableCities in form of City and String
                    unreachableCitiesInStr.add(i);
                    unreachableCities.add(new City(Double.MAX_VALUE,i,new City("NONE",-1,-1)));
                }
            }
        }
        allCitiesWithParents.addAll(finalCheckedCities);// adding all the cities with parent value to an ArrayList
        allCitiesWithParents.addAll(unreachableCities);
        allCitiesWithParents.add(new City(0,startingCity,emptyCity));
        
        
        ArrayList<String> pathToDraw = new ArrayList<>(); // saving the path in order to draw it later
        boolean needToPrint=true; // check value that control that I should print path connections
        City destinationCityInCity=new City(Double.MAX_VALUE,destinationCity,emptyCity); // initializing the destination city in form of City
        for (City destination_city : allCitiesWithParents){ //finding the destination value in allCitiesWithParents and taking it
            if (Objects.equals(destination_city.getCityName(), destinationCity)){
                destinationCityInCity=destination_city;
            }
        }
        if (Objects.equals(startingCity,destinationCity)){ // checking whether the starting city and destination city are the same
            StdDraw.enableDoubleBuffering();
            System.out.print("Total Distance: 0.00. "); //printing total distance
            System.out.print("Path: " + destinationCityInCity.getCityName());
            needToPrint=false; // no need to draw path connections
            drawEverything(startingCity,destinationCity); // drawing the canvas with all the cities and their connections
            StdDraw.show();
        }
        else if (unreachableCitiesInStr.contains(destinationCity)){ // checking whether the destination city is reachable or not
            System.out.println("No path could be found.");
            needToPrint=false; // no need to draw path connections
        }
        else {
            StdDraw.enableDoubleBuffering();
            String totalDistanceStr =String.format("Total Distance: %.2f ",destinationCityInCity.getDistanceValue()); //formatting total distance
            System.out.print(totalDistanceStr);//printing total distance
            ArrayList<City> path = new ArrayList<>(); // initializing the path ArrayList in order to save the path
            City tmpCity=destinationCityInCity.getParent(); //initializing the destination city as tmpCity
            path.add(tmpCity); // adding the destination city to the path
            while (true){
                if (Objects.equals(tmpCity.getCityName(), startingCity)){ //look at all parents until you reach the starting city
                    break;
                }
                else{
                    tmpCity = tmpCity.getParent();//updating the tmpCity
                    path.add(tmpCity);// adding the tmpCity to the path
                }

            }
            String[] pathArray = new String[path.size() + 1]; //creating the pathArray in order to reverse the path ArrayList
            int j = 0;
            for (int i = path.size() - 1; i >= 0; i--) { //reversing code
                pathArray[j] = path.get(i).getCityName();
                pathToDraw.add(j,path.get(i).getCityName());
                j++;
            }
            pathArray[path.size()] = destinationCity; // finally, adding the destination city
            pathToDraw.add(path.size(),destinationCity);
            System.out.print(" Path: ");
            int tmp=0;
            for (String pathCities : pathArray) {// printing the pathArray in a way that as desired
                if (tmp==pathArray.length-1){
                    System.out.print(pathCities);
                }
                else {
                    System.out.print(pathCities + "->");
                    tmp++;
                }
            }
        }
        if (needToPrint){// if I need to print path connections
            StdDraw.enableDoubleBuffering();
            drawEverything(startingCity,destinationCity);// drawing the canvas with all the cities and their connections
            int firstCityIndex=0;
            int secondCityIndex=1;
            while (secondCityIndex<=pathToDraw.size()-1){//Drawing connections looking at all ordered pairs in the pathToDraw
                String firstCityName=pathToDraw.get(firstCityIndex);
                String secondCityName=pathToDraw.get(secondCityIndex);
                int fX = 0;
                int fY = 0;
                int lX = 0;
                int lY = 0;
                String fName=startingCity; //initializing the fName as startingCity
                String lName=destinationCity; //initializing the lName as destinationCity
                ArrayList<City> forFindingPathCoordinates = new ArrayList<>();
                Scanner scanner = new Scanner(new FileInputStream("city_coordinates.txt"));
                while (scanner.hasNextLine()) { // saving the all connections for finding the path cities coordinates
                    String line = scanner.nextLine();
                    String[] lineSplit = line.split(", "); //splitting each line with ', '
                    forFindingPathCoordinates.add(new City(lineSplit[0], Integer.parseInt(lineSplit[1]), Integer.parseInt(lineSplit[2]))); //adding each City according to their name and X,Y coordinates
                }
                for (City i : forFindingPathCoordinates){
                    if (Objects.equals(i.getCityName(),firstCityName)){//when you find the name of the path city, save the coordinates and name of that city
                        fX=i.getX();
                        fY=i.getY();
                        fName=i.getCityName();
                    }
                    if (Objects.equals(i.getCityName(), secondCityName)) {//when you find the name of the path city, save the coordinates and name of that city
                        lX = i.getX();
                        lY = i.getY();
                        lName=i.getCityName();
                    }
                }
                StdDraw.setFont(new Font("Helvetica Bold", Font.BOLD, 12));
                StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE); // setting the pen color as blue
                StdDraw.text(fX, fY + 10, fName);//printing the cities name above
                StdDraw.text(lX, lY + 10, lName);
                StdDraw.setPenRadius(0.009);
                StdDraw.line(fX,fY,lX,lY); // drawing the path cities connections with bold blue lines
                firstCityIndex++;// incrementing the indexes in order to check all path cities
                secondCityIndex++;
                scanner.close(); //closing the scanner
            }
            StdDraw.show();
        }
    }

    /**
     *
     * @param city1 is the first city to evaluate distance between them
     * @param city2  is the second city to evaluate distance between them
     * @return distance between two given cities (city1 and city2)
     * @throws FileNotFoundException file handling exception
     */

    public static double distanceBetween(String city1, String city2) throws FileNotFoundException { // method that returns the distance between two given cities
        ArrayList<City> ct = new ArrayList<>(); //creating the cities ArrayList
        Scanner sc = new Scanner(new FileInputStream("city_coordinates.txt")); //scanning 'city_coordinates.txt' file
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] lineSplit = line.split(", "); //splitting each line with
            ct.add(new City(lineSplit[0], Integer.parseInt(lineSplit[1]), Integer.parseInt(lineSplit[2]))); //adding each City according to their name and X,Y coordinates
        }
        int fX = 0;//initializing the city1's coordinates
        int fY = 0;
        int lX = 0;//initializing the city2's coordinates
        int lY = 0;
        for (City i : ct) {
            if (Objects.equals(i.getCityName(), city1)) {// when you find the name of the city, save the coordinates of that city
                fX = i.getX();
                fY = i.getY();
            }
            if (Objects.equals(i.getCityName(), city2)) { // when you find the name of the city, save the coordinates of that city
                lX = i.getX();
                lY = i.getY();
            }
        }
        sc.close(); //closing the scanner
        return Math.sqrt(Math.pow(Math.abs(fX - lX), 2) + Math.pow(fY - lY, 2)); //calculating and returning the distance between 2 X and Y coordinates
    }

    /**
     *
     * @param cityName is the city that we want to know its adjacent
     * @param previousCity is the city before the first city that called cityName
     * @return ArrayList of adjacent cities of cityName
     * @throws FileNotFoundException file handling exception
     */
    public static ArrayList<String> adjacentCities(String cityName, String previousCity) throws FileNotFoundException { // returning the adjacent cities of given city
        ArrayList<String> adjacentCities1 = new ArrayList<>();
        Scanner connects = new Scanner(new FileInputStream("city_connections.txt")); //scanning 'city_connections.txt' file
        while (connects.hasNextLine()) { // checking all connections in city_connections file
            String line = connects.nextLine();
            String[] lineSplit = line.split(",");
            String cCity = lineSplit[0];
            String adjCity = lineSplit[1];
            if (Objects.equals(cCity, cityName)) {
                if (!Objects.equals(adjCity, previousCity)) {// checking whether the city is the previous city or not, because I don't want to go back to previously checked cities again.
                    adjacentCities1.add(adjCity);
                }
            }
            if (Objects.equals(adjCity, cityName)) {
                if (!Objects.equals(cCity, previousCity)) {// checking whether the city is the previous city or not, because I don't want to go back to previously checked cities again.
                    adjacentCities1.add(cCity);
                }
            }
        }
        connects.close(); //closing the scanner
        return adjacentCities1;// returning all the adjacent cities of given city
    }

    /**
     *
     * @param startingCity is city that given by user
     * @param destinationCity is city that given by user
     * @throws FileNotFoundException file handling exception
     */


    public static void drawEverything(String startingCity, String destinationCity) throws FileNotFoundException { // method that draw basic things such as canvas,map , cities and their connections etc.
        ArrayList<City> cities = new ArrayList<>(); //creating the cities ArrayList
        Scanner sc = new Scanner(new FileInputStream("city_coordinates.txt")); //scanning 'city_coordinates.txt' file
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] lineSplit = line.split(", "); //splitting each line with
            cities.add(new City(lineSplit[0], Integer.parseInt(lineSplit[1]), Integer.parseInt(lineSplit[2]))); //adding each City according to their name and X,Y coordinates
        }
        sc.close(); //closing the scanner
        StdDraw.setCanvasSize(2377 / 2, 1055 / 2); //setting the canvas variables according to 'map.png'
        StdDraw.setXscale(0, 2377);
        StdDraw.setYscale(0, 1055);
        StdDraw.picture(2377 / 2.0, 1055 / 2.0, "map.png", 2377, 1055); //drawing the 'map.png'
        for (City c : cities) {
            if (Objects.equals(c.getCityName(), startingCity) || Objects.equals(c.getCityName(), destinationCity)) { // checking the city whet it is a destination or starting city
                StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE); //setting the pen color
            } else {
                StdDraw.setPenColor(Color.GRAY); //setting the pen color
            }
            StdDraw.filledCircle(c.getX(), c.getY(), 6); // drawing the starting and destination cities
            StdDraw.setFont(new Font("Helvetica Bold", Font.BOLD, 12));//setting the font and font size
            StdDraw.text(c.getX(), c.getY() + 10, c.getCityName()); // printing the city's name above it
        }
        Scanner scn = new Scanner(new FileInputStream("city_connections.txt")); //scanning 'city_connections.txt' file
        while (scn.hasNextLine()) {
            String line = scn.nextLine();
            String[] lineSplit = line.split(",");//splitting each line with
            String first = lineSplit[0]; //initializing cities at either end of the line
            String last = lineSplit[1];
            int fX = 0; //initializing cities coordinates at either end of the line
            int fY = 0;
            int lX = 0;
            int lY = 0;
            for (City c : cities) {
                if (Objects.equals(first, c.getCityName())) { //finding the cities coordinates in the cities ArrayList
                    fX = c.getX();
                    fY = c.getY();
                }
                if (Objects.equals(last, c.getCityName())) { //finding the cities coordinates in the cities ArrayList
                    lX = c.getX();
                    lY = c.getY();
                }
            }
            StdDraw.setPenRadius(0.002); // setting the pen radius
            StdDraw.setPenColor(Color.GRAY); //setting the pen color
            StdDraw.line(fX, fY, lX, lY); // drawing all the lines between connected cities
        }
        scn.close(); //closing the scanner
    }
}