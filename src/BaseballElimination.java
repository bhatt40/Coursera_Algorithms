import java.util.ArrayList;

public class BaseballElimination {

    private class team {

        private int wins;
        private int losses;
        private int remaining;
        private int index;
        private boolean foundEliminators;
        private Bag<String> eliminators;

        protected team(int wins, int losses, int remaining, int index) {

            this.wins = wins;
            this.losses = losses;
            this.remaining = remaining;
            this.index = index;
            this.foundEliminators = false;
            this.eliminators = new Bag<String>();
        }
    }

    private int N;
    private BinarySearchST<String, team> teams;
    private int[][] remainingGames;
    private FlowNetwork flowNetwork;


    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {

        // Read file
        In teamsIn = new In(filename);

        this.N = teamsIn.readInt();
        this.teams = new BinarySearchST<String, team>();

        String teamName;
        int wins, losses, remaining;
        remainingGames  = new int[N][N];
        String[] teamNames = new String[N];

        for (int a = 0; a < this.N; a++) {

            teamName = teamsIn.readString();
            wins = teamsIn.readInt();
            losses = teamsIn.readInt();
            remaining = teamsIn.readInt();

            for (int b = 0; b < this.N; b++) {
                remainingGames[a][b] = teamsIn.readInt();
            }

            teams.put(teamName, new team(wins, losses, remaining, a));
            teamNames[a] = teamName;
        }

        
    }

    // number of teams
    public int numberOfTeams() {

        return this.N;
    }

    // all teams
    public Iterable<String> teams() {

        return this.teams.keys();
    }

    // number of wins for given team
    public int wins(String team) {

        this.checkTeam(team);

        return this.teams.get(team).wins;
    }

    // number of losses for given team
    public int losses(String team) {

        this.checkTeam(team);

        return this.teams.get(team).losses;
    }

    // number of remaining games for given team
    public int remaining(String team){

        this.checkTeam(team);

        return this.teams.get(team).remaining;
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {

        this.checkTeam(team1);
        this.checkTeam(team2);

        return this.remainingGames[teams.get(team1).index][teams.get(team2).index];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {

        this.checkTeam(team);

        // Check for trivial elimination
        for (String a : this.teams.keys()) {
            if((this.teams.get(team).wins + this.teams.get(team).remaining) < this.teams.get(a).wins) {
                this.teams.get(team).eliminators.add(a);
            }
        }

        if (!this.teams.get(team).eliminators.isEmpty()) {
            this.teams.get(team).foundEliminators = true;
            return true;
        }

        // Create a new flowNetwork for this team to check for nontrivial elimination
        this.buildFlowNetwork();

        int teamIndex = teams.get(team).index;
        FlowEdge flowEdge;

        // Edge from vertex for team a to sink
        for(int a = 0; a < N; a++) {
            String teamA = this.getTeamByIndex(a);

            flowEdge = new FlowEdge(teamVertexIndex(a), (N * N + N + 4) / 2 - 1, teams.get(team).wins + teams.get(team).remaining - teams.get(teamA).wins);
            flowNetwork.addEdge(flowEdge);
        }

        // Create FordFulkerson to find max flow/min cut
        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, 0, (N * N + N + 4) / 2 - 1);

        // Find team vertices on source side of min cut
        for(int a = 0; a < N; a++) {
            if (fordFulkerson.inCut(teamVertexIndex(a))) {
                this.teams.get(team).eliminators.add(getTeamByIndex(a));
            }
        }

        this.teams.get(team).foundEliminators = true;
        return (!this.teams.get(team).eliminators.isEmpty());
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {

        this.checkTeam(team);

        if (!this.teams.get(team).foundEliminators)
            this.isEliminated(team);

        if (this.teams.get(team).eliminators.isEmpty()) {
            return null;
        } else
            return this.teams.get(team).eliminators;
    }

    private int teamVertexIndex(int a) {

        return (N*N - N + 2)/2 + a;
    }
    
    private void buildFlowNetwork() {
        
        // Create generic FlowNetwork
        this.flowNetwork = new FlowNetwork((N*N + N + 4)/2);

        // Add edges
        int gamesVertexCounter = 1;
        FlowEdge flowEdge;

        for(int a = 0; a < N; a++) {
            for (int b = a + 1; b < N; b++) {
                // Edge from source to vertex for remaining games between a & b
                flowEdge = new FlowEdge(0, gamesVertexCounter, remainingGames[a][b]);
                flowNetwork.addEdge(flowEdge);

                // Edge from vertex for remaining games between a & b to vertex for team a
                flowEdge = new FlowEdge(gamesVertexCounter, teamVertexIndex(a), Double.POSITIVE_INFINITY);
                flowNetwork.addEdge(flowEdge);

                // Edge from vertex for remaining games between a & b to vertex for team b
                flowEdge = new FlowEdge(gamesVertexCounter, teamVertexIndex(b), Double.POSITIVE_INFINITY);
                flowNetwork.addEdge(flowEdge);

                gamesVertexCounter++;
            }
        }
    }

    private String getTeamByIndex(int index) {

        String team = new String("");

        for (String a : teams.keys()) {
            if (teams.get(a).index == index)
                team = a;
        }

        if (team.compareTo("") == 0)
            throw new IllegalArgumentException();

        return team;
    }

    private void checkTeam(String team) {

        if(!this.teams.contains(team))
            throw new IllegalArgumentException();
    }

    public static void main(String[] args) {

        BaseballElimination baseballElimination = new BaseballElimination(args[0]);

        String team = new String("Ghaddafi");

        StdOut.println(baseballElimination.isEliminated(team));
        for (String a : baseballElimination.certificateOfElimination(team)) {
            StdOut.println(a);
        }
    }
}



