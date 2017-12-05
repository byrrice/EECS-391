import java.util.*;

public class BeamSolver {

    private boardNode goal;

    //boardNode that stores the number of moves, the board, and the previous board
    private class boardNode {
        private int movenumber;
        private RubikTwo board;
        private boardNode prev;

        //gives hashcode of rubik's cube
        @Override
        public int hashCode() {
            return this.board.hashCode();
        }

        //if two rubik's cubes are equal
        @Override
        public boolean equals(Object obj) {
            return this.board.equals(obj);
        }

        //constructor for boardNode
        public boardNode(RubikTwo start) {
            movenumber = 0;
            prev = null;
            board = start;
        }
    }

    //constructor for beamsolver takes rubik's cube, heuristic and beam width, calls solve method
    public BeamSolver(RubikTwo r, String heuristic, int k){
        solve(r, heuristic, k);
    }

    //Does beam search given three inputs
    public void solve(RubikTwo r, String heuristic, int k) {

        //initialize the sets
        Set<boardNode> kbest = new HashSet<boardNode>();
        Set<RubikTwo> seen = new HashSet<RubikTwo>();
        kbest.add(new boardNode(r));
        seen.add(r);
        long visitednodes = 0;
        long maxnodes = r.getMaxNodes();
        PriorityOrder order = new PriorityOrder(heuristic);
        boolean found = false;
        while (visitednodes < maxnodes && !kbest.contains(r) && !found) {
            PriorityQueue<boardNode> queue = new PriorityQueue<boardNode>(order);
            for (boardNode b : kbest) {
                for (RubikTwo child : b.board.neighbors()) {
                    if (!seen.contains(child)) {
                        boardNode n = new boardNode(child);
                        n.movenumber = b.movenumber + 1;
                        n.prev = b;
                        queue.add(n);
                        seen.add(child);
                    }
                    if (child.isGoal()){
                        goal = new boardNode(child);
                        goal.movenumber = b.movenumber + 1;
                        goal.prev = b;
                        queue.add(goal);
                        seen.add(child);
                        found = true;
                    }
                    visitednodes++;
                }
            }
            for (int i = 0; i < k && !queue.isEmpty(); i++) {
                kbest.add(queue.remove());
            }
        }
        System.out.println("Visited Nodes: " + visitednodes);
    }

    //ordering the respective boardNodes by the respective heuristic
    private class PriorityOrder implements Comparator<boardNode> {
        private String heuristic;
        private double nheuristic;
        private double mheuristic;

        private PriorityOrder(String s) {
            this.heuristic = s;
        }

        public int compare(boardNode m, boardNode n) {
            if (heuristic.equals("h1")) {
                mheuristic = m.board.hamming() + m.movenumber;
                nheuristic = n.board.hamming() + n.movenumber;
            }
            if (heuristic.equals("h2")) {
                mheuristic = m.board.manhattan() + m.movenumber;
                nheuristic = n.board.manhattan() + n.movenumber;
            }
            if (mheuristic > nheuristic) {
                return 1;
            }
            if (nheuristic > mheuristic) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    //is it solvable
    public boolean isSolvable() {
        return goal != null;
    }

    //returns the number of moves needed otherwise -1
    public int movenumber() {
        if (!isSolvable())
            return -1;
        else
            return goal.movenumber;
    }

    //gives list of moves by going through queue
    public String solution() {
        if (!isSolvable())
            return null;
        StringBuilder s = new StringBuilder();
        for (boardNode n = goal; n.prev != null; n = n.prev){
            RubikTwo prevBoardU = new RubikTwo(new String(n.prev.board.getCube()));
            prevBoardU.move("U");

            RubikTwo prevBoardUPrime = new RubikTwo(new String(n.prev.board.getCube()));
            prevBoardUPrime.move("U'");

            RubikTwo prevBoardF = new RubikTwo(new String(n.prev.board.getCube()));
            prevBoardF.move("F");

            RubikTwo prevBoardFPrime = new RubikTwo(new String(n.prev.board.getCube()));
            prevBoardFPrime.move("F'");

            RubikTwo prevBoardR = new RubikTwo(new String(n.prev.board.getCube()));
            prevBoardR.move("R");

            RubikTwo prevBoardRPrime = new RubikTwo(new String(n.prev.board.getCube()));
            prevBoardRPrime.move("R'");

            if (n.board.toString().equals(prevBoardF.toString()))
                s.append("F");
            if (n.board.toString().equals(prevBoardFPrime.toString()))
                s.append("F'");
            if (n.board.toString().equals(prevBoardU.toString()))
                s.append("U");
            if (n.board.toString().equals(prevBoardUPrime.toString()))
                s.append("U'");
            if (n.board.toString().equals(prevBoardR.toString()))
                s.append("R");
            if (n.board.toString().equals(prevBoardRPrime.toString()))
                s.append("R'");
        }
        return s.toString();
    }
}
