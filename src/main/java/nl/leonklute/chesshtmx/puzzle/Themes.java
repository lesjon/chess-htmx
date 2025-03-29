package nl.leonklute.chesshtmx.puzzle;

public enum Themes {
    XRAYATTACK("xRayAttack"),
    VUKOVICMATE("vukovicMate"),
    HANGINGPIECE("hangingPiece"),
    MATEIN4("mateIn4"),
    DISCOVEREDATTACK("discoveredAttack"),
    PROMOTION("promotion"),
    HOOKMATE("hookMate"),
    QUEENROOKENDGAME("queenRookEndgame"),
    QUEENENDGAME("queenEndgame"),
    ADVANCEDPAWN("advancedPawn"),
    MATEIN2("mateIn2"),
    SKEWER("skewer"),
    MATEIN3("mateIn3"),
    ENDGAME("endgame"),
    BACKRANKMATE("backRankMate"),
    BODENMATE("bodenMate"),
    CAPTURINGDEFENDER("capturingDefender"),
    CLEARANCE("clearance"),
    PIN("pin"),
    ATTACKINGF2F7("attackingF2F7"),
    DEFLECTION("deflection"),
    SUPERGM("superGM"),
    MATE("mate"),
    LONG("long"),
    UNDERPROMOTION("underPromotion"),
    MATEIN5("mateIn5"),
    MASTERVSMASTER("masterVsMaster"),
    ARABIANMATE("arabianMate"),
    QUEENSIDEATTACK("queensideAttack"),
    DOUBLECHECK("doubleCheck"),
    ANASTASIAMATE("anastasiaMate"),
    SMOTHEREDMATE("smotheredMate"),
    ENPASSANT("enPassant"),
    VERYLONG("veryLong"),
    INTERFERENCE("interference"),
    ADVANTAGE("advantage"),
    QUIETMOVE("quietMove"),
    ZUGZWANG("zugzwang"),
    KILLBOXMATE("killBoxMate"),
    OPENING("opening"),
    MIDDLEGAME("middlegame"),
    DOVETAILMATE("dovetailMate"),
    SACRIFICE("sacrifice"),
    KNIGHTENDGAME("knightEndgame"),
    EQUALITY("equality"),
    TRAPPEDPIECE("trappedPiece"),
    ONEMOVE("oneMove"),
    SHORT("short"),
    MATEIN1("mateIn1"),
    ROOKENDGAME("rookEndgame"),
    DEFENSIVEMOVE("defensiveMove"),
    EXPOSEDKING("exposedKing"),
    MASTER("master"),
    INTERMEZZO("intermezzo"),
    FORK("fork"),
    DOUBLEBISHOPMATE("doubleBishopMate"),
    BISHOPENDGAME("bishopEndgame"),
    KINGSIDEATTACK("kingsideAttack"),
    PAWNENDGAME("pawnEndgame"),
    CASTLING("castling"),
    CRUSHING("crushing"),
    ATTRACTION("attraction");

    private final String name;

    private Themes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
