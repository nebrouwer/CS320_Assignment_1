// Nick Brouwer
// CS 320
// Assignment 1

package Assignment_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.*;

public class BusRoute {
    public static void getBusRoutes() throws IOException {
        //URL connection to search main bus schedules
        URLConnection bus = new URL("https://www.communitytransit.org/busservice/schedules/").openConnection();
        bus.setRequestProperty("user-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        BufferedReader in = new BufferedReader(new InputStreamReader(bus.getInputStream()));
        String inputLine = "";
        String text = "";
        while ((inputLine = in.readLine()) != null) {
            text += inputLine ;
        }in.close();

        //Accepts and reads input from user for destination desired
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter uppercase first letter of destination: ");
        String input = scan.next();

        //Pattern for searching main web page for desired destinations
        Pattern destination = Pattern.compile("<h3>(" + input + ".*?)</h3>(.*?)<hr id", Pattern.DOTALL);
        Matcher destM = destination.matcher(text);
        Hashtable routeInfo = new Hashtable();
        while(destM.find()){
            System.out.println("Destination: " + destM.group(1));
            String destRoute = destM.group(2);
            Pattern route = Pattern.compile("<strong><a href=\"(.*?)\".*?>(.*?)</a></strong>"); //Pattern for finding route schedule URL link and route number within desired destination
            Matcher routeM = route.matcher(destRoute);

            while(routeM.find()){
                System.out.println("Route Number: " + routeM.group(2));
                routeInfo.put(routeM.group(2), "https://www.communitytransit.org/busservice" + routeM.group(1));  //Saves route number and URL link in hashtable
            }
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++");

        }
        //Accepts input from user for desired route number
        Scanner scan2 = new Scanner(System.in);
        System.out.print("Please enter a route number: ");
        String routeID = scan2.next();
        String routeLink = String.valueOf(routeInfo.get(routeID));

        System.out.println(routeLink);

        //URL connection for route schedule
        URLConnection link = new URL(routeLink).openConnection();
        link.setRequestProperty("user-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        in = new BufferedReader(new InputStreamReader(link.getInputStream()));
        String inputLine2 = "";
        String text2 = "";
        while ((inputLine2 = in.readLine()) != null) {
            text2 += inputLine2 + "\n";
        }in.close();

        //Pattern for finding destinations for desired route number input
        Pattern schedule = Pattern.compile("<h2>.*?<small>(.*?)</small></h2>(.*?)</thead>", Pattern.DOTALL);
        Matcher scheduleMatch = schedule.matcher(text2);

        while(scheduleMatch.find()){
            System.out.println("Destination: " + scheduleMatch.group(1));
            String destCity = scheduleMatch.group(2);
            //Pattern for finding bus stop locations
            Pattern destSchedule = Pattern.compile("<strong class=.*?>(.*?)</strong>.*?<p>(.*?)</p>", Pattern.DOTALL);
            Matcher busStop = destSchedule.matcher(destCity);

            while(busStop.find()){
                System.out.println("Stop number: " + busStop.group(1) + " is " + busStop.group(2).replaceAll("&amp;", "&"));
            }
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
        }
    }
}
