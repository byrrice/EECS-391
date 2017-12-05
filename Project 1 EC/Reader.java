import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

public class Reader {
    public static void main(String[] args) {

        //the new rubik's cube
        RubikTwo r = new RubikTwo();

        //set seed
        Random randomno = new Random();
        randomno.setSeed(20);


        //takes in the args and does the necessary methods
        if (args[0].equals("toString")) {
            System.out.println(r.toString());
        }
        else if (args[0].equals("randomizeState")) {
            r.randomizeState(Integer.parseInt(args[1]));
            System.out.println("Randomized " + args[1]);
        }
        else if (args[0].equals("setState")) {
            r.setState(args[1]);
            System.out.println("set state to  " + args[1]);
        }
        else if (args[0].equals("move")) {
            r.move(args[1]);
            System.out.println("Moved " + args[1]);
        }
        else if (args[0].equals("solve") && args[1].equals("A-star")) {
            StarSolver s = new StarSolver(r, args[2]);
            System.out.println(s.solution());
            System.out.println(s.movenumber());
        }

        else if (args[0].equals("solve") && args[1].equals("beam")){
            BeamSolver bs1 = new BeamSolver(r, args[2], Integer.parseInt(args[3]));
            System.out.println(bs1.solution());
            System.out.println(bs1.movenumber());
        }

        else if (args[0].equals("maxNodes")){
            r.setMaxNodes(Integer.parseInt(args[1]));
            System.out.println("Max Nodes: " + args[1]);
        }

        //checks for txt file
        else if (args.length == 1 && !args[0].equals("toString")){
                execute(r, args[0]);

        }
    }


    //executes txt file command lines
    public static void execute(RubikTwo r, String s) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(s));
            //takes in line by line commands and executes them depending on their input
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                String[] args = line.split(" ");
                if (args[0].equals("toString"))
                    System.out.println(r.toString());
                else if (args[0].equals("randomizeState")) {
                    r.randomizeState(Integer.parseInt(args[1]));
                    System.out.println("Randomized " + args[1]);
                }
                else if (args[0].equals("move")) {
                    r.move(args[1]);
                    System.out.println("Moved " + args[1]);
                }
                else if (args[0].equals("setState")) {
                    r.setState(args[1]);
                    System.out.println("set state to  " + args[1]);
                }
                else if (args[0].equals("solve") && args[1].equals("A-star")) {
                    StarSolver s2 = new StarSolver(r, args[2]);
                    System.out.println(s2.solution());
                    System.out.println(s2.movenumber());

                } else if (args[0].equals("solve") && args[1].equals("beam")) {
                    BeamSolver bs2 = new BeamSolver(r, args[2], Integer.parseInt(args[3]));
                    System.out.println(bs2.solution());
                    System.out.println(bs2.movenumber());
                } else if (args[0].equals("setMaxNodes")) {
                    r.setMaxNodes(Long.parseLong(args[1]));
                    System.out.println("Max Nodes: " + args[1]);
                }
                else
                    System.out.println("Error! Please enter valid command! ");
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Error opening file!");
        }
    }

}
