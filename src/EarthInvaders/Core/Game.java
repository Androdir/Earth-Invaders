package EarthInvaders.Core;

import EarthInvaders.GUIs.Help;
import EarthInvaders.GUIs.Menu;
import EarthInvaders.GUIs.SelectMode;
import EarthInvaders.Interfaces.*;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Timer;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Game extends Canvas implements Runnable {

    public static final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    public static final int WIDTH = gd.getDisplayMode().getWidth(); // get screen resolution
    public static final int HEIGHT = gd.getDisplayMode().getHeight();
    public static final String TITLE = "Earth Invaders";

    private static final int defaultHeight = 1080;
    private static final int defaultWidth = 1920;
    public static final float percentageHeight = (float) Game.HEIGHT / defaultHeight;
    public static final float percentageWidth = (float) Game.WIDTH / defaultWidth;
    // percentages to scale game to different sizes

    public JFrame frame;
    public JPanel panel;

    private final BufferedImage cursorImg = new BufferedImage(4, 4, BufferedImage.TYPE_INT_ARGB);
    private final Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");

    private boolean running = false;
    private Thread thread;

    private BufferedImage spriteSheet = null;
    private BufferedImage gameBackground = null;
    private BufferedImage menuBackground = null;

    private Textures t;
    public Player p1;
    public Player p2;
    private Controller c;
    private Menu menu;
    private Help help;
    private SelectMode selectMode;

    private final File attackSound1 = new File("res\\attack_sound_1.wav");
    private final File clickSound = new File("res\\click.wav");
    private final File explosionSound = new File("res\\explosion_sound_2.wav");
    private final File bgm1Sound = new File("res\\background_music_1.wav");
    private final File bgm2Sound = new File("res\\background_music_2.wav");
    private final File bossShootSound = new File("res\\bossShoot.wav");
    private final File bossSpawnSound = new File("res\\bossAlert.wav");
    private final File playerDeathSound = new File("res\\playerDeath.wav");
    private final File playerHurtSound = new File("res\\playerHurtSound.wav");
    private final File powerUpSound = new File("res\\powerUpSound.wav");

    private AudioInputStream attackSound1Stream;
    private AudioInputStream clickSoundStream;
    private AudioInputStream explosionSoundStream;
    private AudioInputStream bgm1Stream;
    private AudioInputStream bgm2Stream;
    private AudioInputStream bossShootStream;
    private AudioInputStream bossSpawnStream;
    private AudioInputStream playerDeathStream;
    private AudioInputStream playerHurtStream;
    private AudioInputStream powerUpStream;

    public static Clip ATTACK_1;
    public static Clip CLICK;
    public static Clip EXPLOSION;
    public static Clip BGM_1;
    public static Clip BGM_2;
    public static Clip BOSS_SHOOT;
    public static Clip BOSS_SPAWN_ALERT;
    public static Clip PLAYER_DEATH;
    public static Clip PLAYER_HURT;
    public static Clip POWER_UP;

    public static boolean PLAY_BG_MUSIC = true;
    public static boolean PLAY_SFX = true;

    {
        try {
            attackSound1Stream = AudioSystem.getAudioInputStream(attackSound1);
            ATTACK_1 = AudioSystem.getClip();
            ATTACK_1.open(attackSound1Stream);

            clickSoundStream = AudioSystem.getAudioInputStream(clickSound);
            CLICK = AudioSystem.getClip();
            CLICK.open(clickSoundStream);

            explosionSoundStream = AudioSystem.getAudioInputStream(explosionSound);
            EXPLOSION = AudioSystem.getClip();
            EXPLOSION.open(explosionSoundStream);

            bgm1Stream = AudioSystem.getAudioInputStream(bgm1Sound);
            BGM_1 = AudioSystem.getClip();
            BGM_1.open(bgm1Stream);

            bgm2Stream = AudioSystem.getAudioInputStream(bgm2Sound);
            BGM_2 = AudioSystem.getClip();
            BGM_2.open(bgm2Stream);

            bossShootStream = AudioSystem.getAudioInputStream(bossShootSound);
            BOSS_SHOOT = AudioSystem.getClip();
            BOSS_SHOOT.open(bossShootStream);

            bossSpawnStream = AudioSystem.getAudioInputStream(bossSpawnSound);
            BOSS_SPAWN_ALERT = AudioSystem.getClip();
            BOSS_SPAWN_ALERT.open(bossSpawnStream);

            playerDeathStream = AudioSystem.getAudioInputStream(playerDeathSound);
            PLAYER_DEATH = AudioSystem.getClip();
            PLAYER_DEATH.open(playerDeathStream);

            playerHurtStream = AudioSystem.getAudioInputStream(playerHurtSound);
            PLAYER_HURT = AudioSystem.getClip();
            PLAYER_HURT.open(playerHurtStream);

            powerUpStream = AudioSystem.getAudioInputStream(powerUpSound);
            POWER_UP = AudioSystem.getClip();
            POWER_UP.open(powerUpStream);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // audio  ^^


    // players
    public ArrayList<EntityA> ea;
    // player bullets
    public ArrayList<EntityB> eb;
    // enemies
    public ArrayList<EntityC> ec;
    // enemies' bullets
    public ArrayList<EntityD> ed;
    // power ups
    public ArrayList<EntityE> ee;

    private boolean isShootingP1 = false;
    private boolean isShootingP2 = false;

    public static long startTime;

    // game font
    public static Font gameFont;

    // variables to store temporary data
    public boolean deathMessageWait = false;

    // 1 = player 1 dead, 2 = player 2 dead
    public int dead = 0;

    public boolean paused = false; // true = game is paused, false = game is not paused

    // START OF CORE GAME VARIABLES

    private int enemyCount;    // number of enemies that spawn every round
    private int enemyKilled;     // check if player killed all enemies
    private int level;    // current level
    private int totalKills;    // total kills for stats
    private int score;    // score for stats
    private long timeTaken;    // total time taken for stats
    public long highScore = 0;    // var for high score
    public File levelsHighScoreSave = new File("res\\levels_highscore.txt"); // high score for levels game mode
    public File timedHighScoreSave = new File("res\\timed_highscore.txt");  // high score for timed game mode
    public File infinityHighScoreSave = new File("res\\infinity_highscore.txt"); // high score for infinite game mode
    public static boolean againstFriend = false;    // if true, play against friend
    public static boolean withFriend = false; // if true, 2 players at once
    public static boolean singlePlayer = true; // if true, single player

    // constants
    public static final int TIMED_MAX_SCORE = 300;
    public static final int LEVELS_TO_WIN = 10;
    public static final int PLAYER_BULLET_COOLDOWN = 600;
    public static final long ENEMY_BULLET_COOLDOWN = 1600;
    public static final long BOSS_BULLET_COOLDOWN = 1900;
    public static final long INFINITY_SPAWN_COOLDOWN = 4000;
    public static final long POWER_UP_SPAWN_TIME = 20000;

    public enum GAMEMODE {
        LEVELS, // levels, when you kill all enemies spawn 1 more
        INFINITE, // x enemies spawn every y seconds
        TIMED // reach x points as fast as you can
    }

    public static GAMEMODE gamemode = GAMEMODE.LEVELS;

    public enum PART {
        MENU, // menu
        HELP, // help
        MODES, // select mode menu
        RDYP1, // ready player one
        P1, // player one turn
        GOP1, // game over one
        RDYP2, // ready player two
        P2, // player two turn
        GOP2, // game over two
        WIN, // player beat the game
    }

    public static PART part = PART.MENU;

    // END OF CORE GAME VARIABLES

    public JTextArea helpTextArea;

    private static boolean startP1Timer = true;
    private static boolean startP2Timer = true;

    Timer bulletCooldownP1 = new Timer();
    Timer bulletCooldownP2 = new Timer();

    public void init() {
        requestFocus();

        // font
        try {
            InputStream stream = new FileInputStream("res\\game_font.ttf");
            gameFont = Font.createFont(Font.PLAIN, stream);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        // graphics
        createBufferStrategy(3);
        BufferStrategy bs = this.getBufferStrategy();
        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0,0,Game.WIDTH,Game.HEIGHT);
        g.setFont(gameFont.deriveFont(Font.PLAIN, 150f * percentageWidth));
        g.setColor(Color.WHITE);
        g.drawString("LOADING...", 200, Game.HEIGHT / 2);

        g.dispose();
        bs.show();

        ImageLoader loader = new ImageLoader();
        try {
            spriteSheet = loader.loadImage("res\\sprite_sheet.png");
            gameBackground = loader.loadImage("res\\gameBackground.png");
            menuBackground = loader.loadImage("res\\menuBackground.png");

        } catch (IOException e) {
            e.printStackTrace();
        }

        helpTextArea.setVisible(false);
        helpTextArea.setEditable(false);
        helpTextArea.setLayout(null);
        helpTextArea.setBounds((int) (200 * percentageWidth), (int) (200 * percentageHeight), (int) (Game.WIDTH - 400 * percentageWidth), (int) (Game.HEIGHT - 400 * percentageHeight));
        helpTextArea.setLayout(null);
        helpTextArea.setFont(Game.gameFont.deriveFont(Font.PLAIN, WIDTH / 60f));
        helpTextArea.setForeground(Color.BLACK);
        helpTextArea.setLineWrap(true);
        helpTextArea.setHighlighter(null);
        helpTextArea.setWrapStyleWord(true);

        part = PART.MENU;

        t = new Textures(this);
        c = new Controller(t, this);
        menu = new Menu(this);
        help = new Help(this);
        selectMode = new SelectMode();

        ea = c.getEntityA();
        eb = c.getEntityB();
        ec = c.getEntityC();
        ed = c.getEntityD();
        ee = c.getEntityE();

        this.addKeyListener(new KeyInput(this));

        Menu menu = new Menu(this);
        Help help = new Help(this);
        SelectMode modes = new SelectMode();

        this.addMouseListener(menu);
        this.addMouseMotionListener(menu);

        this.addMouseListener(help);
        this.addMouseMotionListener(help);

        this.addMouseListener(modes);
        this.addMouseMotionListener(modes);
    }

    private synchronized void start() {
        if (running)
            return;

        running = true;
        thread = new Thread(this);
        thread.start();
    }

    private synchronized void stop() {
        if (!running)
            return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public void run() {

        init();
        long lastTime = System.nanoTime();
        final double amountOfTicks = 60;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        int updates = 0;
        int frames = 0;
        long timer = System.currentTimeMillis();

        final Timer spawnPowerUp = new Timer();
        spawnPowerUp.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                c.addEntity(new PowerUp(ThreadLocalRandom.current().nextInt(64, WIDTH - 64 + 1), ThreadLocalRandom.current().nextInt((HEIGHT - HEIGHT / 4), HEIGHT + 1 - 64), c, t));
            }
        }, POWER_UP_SPAWN_TIME, POWER_UP_SPAWN_TIME);

        Timer infiniteSpawn = new Timer();
        infiniteSpawn.schedule(new TimerTask() {
            public void run() {
                if ((part == PART.P1 || part == PART.P2) && gamemode == GAMEMODE.INFINITE) {
                    final int chance = ThreadLocalRandom.current().nextInt(1, 15 + 1);
                    if (chance != 15) {
                        c.createEnemy(2);
                    } else {
                        c.spawnBoss(1);
                    }
                    if (withFriend)
                        c.createEnemy(1);
                }
            }
        }, INFINITY_SPAWN_COOLDOWN, INFINITY_SPAWN_COOLDOWN);

        BGM_2.stop();
        BGM_2.setFramePosition(0);
        BGM_2.start();

        BGM_1.addLineListener(event -> {
            if (event.getType() == LineEvent.Type.STOP && PLAY_BG_MUSIC)
            {
                BGM_2.setFramePosition(0);
                BGM_2.start();
            }
        });

        BGM_2.addLineListener(event -> {
            if (event.getType() == LineEvent.Type.STOP && PLAY_BG_MUSIC)
            {
                BGM_1.setFramePosition(0);
                BGM_1.start();
            }
        });

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            if (delta >= 1) {
                tick();
                render();
                updates++;
                delta--;
            }


            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println("Ticks: " + updates + " | FPS: " + frames);
                updates = 0;
                frames = 0;
            }
        }

        stop();
    }

    private void tick() {

        if ((part == PART.P1 || part == PART.P2) && !paused) {
            frame.setCursor(blankCursor);
            p1.tick();
            if (withFriend)
                p2.tick();
            c.tick();
        }
        else {
            frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        ////////////////////////////////////

        if (part == PART.P1 || part == PART.P2) {

            g.drawImage(gameBackground, 0 ,0, getWidth(), getHeight(), this);
            g.setFont(gameFont.deriveFont(Font.BOLD, 30f * percentageWidth));
            g.setColor(Color.WHITE);

            FontMetrics fontMetrics = g.getFontMetrics();

            if (gamemode == GAMEMODE.INFINITE) {
                String temp = "Score: " + getScore();
                g.drawString(temp, WIDTH / 2 - fontMetrics.stringWidth(temp) / 2, 20 + fontMetrics.getHeight());
            }
            else if (gamemode == GAMEMODE.TIMED) {
                setTimeTaken(System.currentTimeMillis() - startTime);
                String temp = "Score: " + getScore();
                g.drawString(temp, WIDTH / 2 - fontMetrics.stringWidth(temp) / 2, 20 + fontMetrics.getHeight());
                temp = "Time: " + TimeUnit.MINUTES.convert(getTimeTaken(), TimeUnit.MILLISECONDS) + " mins " + TimeUnit.SECONDS.convert(getTimeTaken(), TimeUnit.MILLISECONDS) % 60 + " secs";
                g.drawString(temp, WIDTH / 2 - fontMetrics.stringWidth(temp) / 2, 20 + 5 + fontMetrics.getHeight() * 2);
                temp = "Level: " + getLevel();
                g.drawString(temp, WIDTH / 2 - fontMetrics.stringWidth(temp) / 2, 20 + 5 + 5 + fontMetrics.getHeight() * 3);
            }
            else if (gamemode == GAMEMODE.LEVELS) {
                String temp = "Score: " + getScore();
                g.drawString(temp, WIDTH / 2 - fontMetrics.stringWidth(temp) / 2, 20 + fontMetrics.getHeight());
                temp = "Level: " + getLevel();
                g.drawString(temp, WIDTH / 2 - fontMetrics.stringWidth(temp) / 2, 20 + 5 + fontMetrics.getHeight() * 2);
            }


            if (c.coordinatesOfExplosions.size() > 0) {
                Iterator<Map.Entry<Integer, Integer>> iterator = c.coordinatesOfExplosions.entrySet().iterator();

                while (iterator.hasNext()) {
                    Map.Entry<Integer, Integer> map = iterator.next();

                    Graphics2D g2d = (Graphics2D) g;
                    g2d.drawImage(t.explosion, map.getKey(), map.getValue(), null);
                    Timer removeExplosion = new Timer();
                    removeExplosion.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            c.explosionsToDelete.add(map);
                            removeExplosion.cancel();
                        }
                    }, c.explosionGifLength);
                }

                if (c.explosionsToDelete.size() > 0) {
                    for (int i = 0; i < c.explosionsToDelete.size(); i++) {
                        c.coordinatesOfExplosions.remove(c.explosionsToDelete.get(i).getKey());
                    }
                }
            }

            if (!withFriend) {

                if (p1.getPowerUps().contains(PowerUp.POWER_UP_TYPE.INCREASED_SPEED)) {
                    g.drawImage(t.powerUpSpeed, (int) (25 * percentageWidth), (int) (80 * percentageWidth), null);
                }
                else if (p1.getPowerUps().contains(PowerUp.POWER_UP_TYPE.SHORTER_ATTACK_COOLDOWN)) {
                    g.drawImage(t.powerUpCooldown, (int) (25 * percentageWidth), (int) (80 * percentageWidth), null);
                }
                else if (p1.getPowerUps().contains(PowerUp.POWER_UP_TYPE.IMMUNITY)) {
                    g.drawImage(t.powerUpImmunity, (int) (25 * percentageWidth), (int) (80 * percentageWidth), null);
                }

                if (p1.getLives() > 4)
                {
                    g.setColor(Color.WHITE);
                    g.fillRect((int) (25 * percentageWidth), (int) (30 * percentageWidth), (int) (150 * percentageWidth), (int) (45 * percentageWidth));
                    g.setColor(Color.GRAY);
                    g.fillRect(((int) (30 * percentageWidth)), ((int) (35 * percentageWidth)), ((int) (140 * percentageWidth)), ((int) (35 * percentageWidth)));
                    g.setColor(new Color(60, 214, 245));
                    g.fillRect(((int) (30 * percentageWidth)), ((int) (35 * percentageWidth)), ((int) (140 * percentageWidth)), ((int) (35 * percentageWidth)));
                    g.setColor(new Color(59, 168, 190));

                    g.drawString("+ " + (p1.getLives() - 4), 175, 65);
                }

                else if (p1.getLives() == 4)
                {
                    g.setColor(Color.WHITE);
                    g.fillRect((int) (25 * percentageWidth), (int) (30 * percentageWidth), (int) (150 * percentageWidth), (int) (45 * percentageWidth));
                    g.setColor(Color.GRAY);
                    g.fillRect((int) (30 * percentageWidth), (int) (35 * percentageWidth), (int) (140 * percentageWidth), (int) (35 * percentageWidth));
                    g.setColor(new Color(60, 214, 245));
                    g.fillRect((int) (30 * percentageWidth), (int) (35 * percentageWidth), (int) (140 * percentageWidth), (int) (35 * percentageWidth));
                }
                else if (p1.getLives() == 3)
                {
                    g.setColor(Color.WHITE);
                    g.fillRect((int) (25 * percentageWidth), (int) (30 * percentageWidth), (int) (150 * percentageWidth), (int) (45 * percentageWidth));
                    g.setColor(Color.GRAY);
                    g.fillRect((int) (30 * percentageWidth), (int) (35 * percentageWidth), (int) (140 * percentageWidth), (int) (35 * percentageWidth));
                    g.setColor(new Color(60, 214, 245));
                    g.fillRect((int) (30 * percentageWidth), (int) (35 * percentageWidth), (int) (140 * 2 / 3 * percentageWidth), (int) (35 * percentageWidth));
                }
                else if (p1.getLives() == 2)
                {
                    g.setColor(Color.WHITE);
                    g.fillRect((int) (25 * percentageWidth), (int) (30 * percentageWidth), (int) (150 * percentageWidth), (int) (45 * percentageWidth));
                    g.setColor(Color.GRAY);
                    g.fillRect((int) (30 * percentageWidth), (int) (35 * percentageWidth), (int) (140 * percentageWidth), (int) (35 * percentageWidth));
                    g.setColor(new Color(60, 214, 245));
                    g.fillRect((int) (30 * percentageWidth), (int) (35 * percentageWidth), (int) (140 / 3 * percentageWidth), (int) (35 * percentageWidth));
                }
                else if (p1.getLives() == 1)
                {
                    g.setColor(Color.WHITE);
                    g.fillRect((int) (25 * percentageWidth), (int) (30 * percentageWidth), (int) (150 * percentageWidth), (int) (45 * percentageWidth));
                    g.setColor(Color.RED);
                    g.fillRect((int) (30 * percentageWidth), (int) (35 * percentageWidth), (int) (140 * percentageWidth), (int) (35 * percentageHeight));
                }
                else
                {
                    g.setColor(Color.WHITE);
                    g.fillRect((int) (25 * percentageWidth), (int) (30 * percentageWidth), (int) (150 * percentageWidth), (int) (45 * percentageWidth));
                    g.setColor(Color.GRAY);
                    g.fillRect((int) (30 * percentageWidth), (int) (35 * percentageWidth), (int) (140 * percentageWidth), (int) (35 * percentageWidth));
                }
            }

            else {

                if (p1.getPowerUps().contains(PowerUp.POWER_UP_TYPE.INCREASED_SPEED)) {
                    g.drawImage(t.powerUpSpeed, (int) (25 * percentageWidth), (int) (80 * percentageWidth), null);
                }
                else if (p1.getPowerUps().contains(PowerUp.POWER_UP_TYPE.SHORTER_ATTACK_COOLDOWN)) {
                    g.drawImage(t.powerUpCooldown, (int) (25 * percentageWidth), (int) (80 * percentageWidth), null);
                }
                else if (p1.getPowerUps().contains(PowerUp.POWER_UP_TYPE.IMMUNITY)) {
                    g.drawImage(t.powerUpImmunity, (int) (25 * percentageWidth), (int) (80 * percentageWidth), null);
                }

                if (p1.getLives() > 4)
                {
                    g.setColor(Color.WHITE);
                    g.fillRect((int) (25 * percentageWidth), (int) (30 * percentageWidth), (int) (150 * percentageWidth), (int) (45 * percentageWidth));
                    g.setColor(Color.GRAY);
                    g.fillRect((int) (30 * percentageWidth), (int) (35 * percentageWidth), (int) (140 * percentageWidth), (int) (35 * percentageWidth));
                    g.setColor(new Color(60, 214, 245));
                    g.fillRect((int) (30 * percentageWidth), (int) (35 * percentageWidth), (int) (140 * percentageWidth), (int) (35 * percentageWidth));
                    g.setColor(new Color(59, 168, 190));
                    g.drawString("+ " + (p1.getLives() - 4), (int) (175 * percentageWidth), (int) (65 * percentageWidth));
                }

                else if (p1.getLives() == 4)
                {
                    g.setColor(Color.WHITE);
                    g.fillRect((int) (25 * percentageWidth), (int) (30 * percentageWidth), (int) (150 * percentageWidth), (int) (45 * percentageWidth));
                    g.setColor(Color.GRAY);
                    g.fillRect((int) (30 * percentageWidth), (int) (35 * percentageWidth), (int) (140 * percentageWidth), (int) (35 * percentageWidth));
                    g.setColor(new Color(60, 214, 245));
                    g.fillRect((int) (30 * percentageWidth), (int) (35 * percentageWidth), (int) (140 * percentageWidth), (int) (35 * percentageWidth));
                }
                else if (p1.getLives() == 3)
                {
                    g.setColor(Color.WHITE);
                    g.fillRect((int) (25 * percentageWidth), (int) (30 * percentageWidth), (int) (150 * percentageWidth), (int) (45 * percentageWidth));
                    g.setColor(Color.GRAY);
                    g.fillRect((int) (30 * percentageWidth), (int) (35 * percentageWidth), (int) (140 * percentageWidth), (int) (35 * percentageWidth));
                    g.setColor(new Color(60, 214, 245));
                    g.fillRect((int) (30 * percentageWidth), (int) (35 * percentageWidth), (int) (140 * 2 / 3 * percentageWidth), (int) (35 * percentageWidth));
                }
                else if (p1.getLives() == 2)
                {
                    g.setColor(Color.WHITE);
                    g.fillRect((int) (25 * percentageWidth), (int) (30 * percentageWidth), (int) (150 * percentageWidth), (int) (45 * percentageWidth));
                    g.setColor(Color.GRAY);
                    g.fillRect((int) (30 * percentageWidth), (int) (35 * percentageWidth), (int) (140 * percentageWidth), (int) (35 * percentageWidth));
                    g.setColor(new Color(60, 214, 245));
                    g.fillRect((int) (30 * percentageWidth), (int) (35 * percentageWidth), (int) (140 / 3 * percentageWidth), (int) (35 * percentageWidth));
                }
                else if (p1.getLives() == 1)
                {
                    g.setColor(Color.WHITE);
                    g.fillRect((int) (25 * percentageWidth), (int) (30 * percentageWidth), (int) (150 * percentageWidth), (int) (45 * percentageWidth));
                    g.setColor(Color.RED);
                    g.fillRect((int) (30 * percentageWidth), (int) (35 * percentageWidth), (int) (140 * percentageWidth), (int) (35 * percentageWidth));
                }

                else
                {
                    g.setColor(Color.WHITE);
                    g.fillRect((int) (25 * percentageWidth), (int) (30 * percentageWidth), (int) (150 * percentageWidth), (int) (45 * percentageWidth));
                    g.setColor(Color.GRAY);
                    g.fillRect((int) (30 * percentageWidth), (int) (35 * percentageWidth), (int) (140 * percentageWidth), (int) (35 * percentageWidth));
                }

                if (p2.getPowerUps().contains(PowerUp.POWER_UP_TYPE.INCREASED_SPEED)) {
                    System.out.println("ye");
                    g.drawImage(t.powerUpSpeed, (int) (WIDTH - 25 - t.powerUpSpeed.getWidth() * percentageWidth), (int) (80 * percentageWidth), null);
                }
                else if (p2.getPowerUps().contains(PowerUp.POWER_UP_TYPE.SHORTER_ATTACK_COOLDOWN)) {
                    System.out.println("ye");
                    g.drawImage(t.powerUpCooldown, (int) (WIDTH - 25 - t.powerUpCooldown.getWidth()  * percentageWidth), (int) (80 * percentageWidth), null);
                }
                else if (p2.getPowerUps().contains(PowerUp.POWER_UP_TYPE.IMMUNITY)) {
                    System.out.println("ye");
                    g.drawImage(t.powerUpImmunity, (int) (WIDTH - 25 - t.powerUpImmunity.getWidth()  * percentageWidth), (int) (80 * percentageWidth), null);
                }


                if (p2.getLives() > 4)
                {
                    g.setColor(Color.WHITE);
                    g.fillRect((int) (WIDTH - 175 * percentageWidth), (int) (30 * percentageWidth), (int) (150 * percentageWidth), (int) (45 * percentageWidth));
                    g.setColor(Color.GRAY);
                    g.fillRect((int) (WIDTH - 170 * percentageWidth), (int) (35 * percentageWidth), (int) (140 * percentageWidth), (int) (35 * percentageWidth));
                    g.setColor(new Color(23, 237, 72));
                    g.fillRect((int) (WIDTH - 170 * percentageWidth), (int) (35 * percentageWidth), (int) (140 * percentageWidth), (int) (35 * percentageWidth));
                    g.setColor(new Color(46, 181, 74));

                    final FontMetrics fm = g.getFontMetrics();

                    g.drawString("+ " + (p2.getLives() - 4), (int) (percentageWidth * WIDTH - 175 - fm.stringWidth("+ " + (p2.getLives() - 1)) - 10), (int) (65 * percentageWidth));
                }

                else if (p2.getLives() == 4)
                {
                    g.setColor(Color.WHITE);
                    g.fillRect((int) (WIDTH - 175 * percentageWidth), (int) (30 * percentageWidth), (int) (150 * percentageWidth), (int) (45 * percentageWidth));
                    g.setColor(Color.GRAY);
                    g.fillRect((int) (WIDTH - 170 * percentageWidth), (int) (35 * percentageWidth), (int) (140 * percentageWidth), (int) (35 * percentageWidth));
                    g.setColor(new Color(23, 237, 72));
                    g.fillRect((int) (WIDTH - 170 * percentageWidth), (int) (35 * percentageWidth), (int) (140 * percentageWidth), (int) (35 * percentageWidth));
                }
                else if (p2.getLives() == 3)
                {
                    g.setColor(Color.WHITE);
                    g.fillRect((int) (WIDTH - 175 * percentageWidth), (int) (30 * percentageWidth), (int) (150 * percentageWidth), (int) (45 * percentageWidth));
                    g.setColor(Color.GRAY);
                    g.fillRect((int) (WIDTH - 170 * percentageWidth), (int) (35 * percentageWidth), (int) (140 * percentageWidth), (int) (35 * percentageWidth));
                    g.setColor(new Color(23, 237, 72));
                    g.fillRect((int) (WIDTH - 170 * percentageWidth), (int) (35 * percentageWidth), (int) (140 * 2 / 3 * percentageWidth), (int) (35 * percentageWidth));
                }
                else if (p2.getLives() == 2)
                {
                    g.setColor(Color.WHITE);
                    g.fillRect((int) (WIDTH - 175 * percentageWidth), (int) (30 * percentageWidth), (int) (150 * percentageWidth), (int) (45 * percentageWidth));
                    g.setColor(Color.GRAY);
                    g.fillRect((int) (WIDTH - 170 * percentageWidth), (int) (35 * percentageWidth), (int) (140 * percentageWidth), (int) (35 * percentageWidth));
                    g.setColor(new Color(23, 237, 72));
                    g.fillRect((int) (WIDTH - 170 * percentageWidth), (int) (35 * percentageWidth), (int) (140 / 3 * percentageWidth), (int) (35 * percentageWidth));
                }
                else if (p2.getLives() == 1)
                {
                    g.setColor(Color.WHITE);
                    g.fillRect((int) (WIDTH - 175 * percentageWidth), (int) (30 * percentageWidth), (int) (150 * percentageWidth), (int) (45 * percentageWidth));
                    g.setColor(Color.RED);
                    g.fillRect((int) (WIDTH - 170 * percentageWidth), (int) (35 * percentageWidth), (int) (140 * percentageWidth), (int) (35 * percentageWidth));
                }
                else
                {
                    g.setColor(Color.WHITE);
                    g.fillRect((int) (WIDTH - 175 * percentageWidth), (int) (30 * percentageWidth), (int) (150 * percentageWidth), (int) (45 * percentageWidth));
                    g.setColor(Color.GRAY);
                    g.fillRect((int) (WIDTH - 170 * percentageWidth), (int) (35 * percentageWidth), (int) (140 * percentageWidth), (int) (35 * percentageWidth));
                }
            }

            if (!(((dead == 1 && !againstFriend) || (dead == 2 && againstFriend)) && deathMessageWait)) {
                p1.render(g);
                c.render(g);
            }

        }
        else if (part == PART.MENU) {
            g.drawImage(menuBackground, 0, 0, getWidth(), getHeight(), this);
            menu.render(g);
            helpTextArea.setVisible(false);
        }
        else if (part == PART.HELP) {
            g.drawImage(menuBackground, 0, 0, getWidth(), getHeight(), this);
            help.render(g);
        }
        else if (part == PART.MODES) {
            g.drawImage(menuBackground, 0, 0, getWidth(), getHeight(), this);
            selectMode.render(g, this);
        }

        else if (part == PART.RDYP1)
            if (!withFriend)
                drawStartingText(g, 1);
            else {
                g.drawImage(menuBackground, 0, 0, WIDTH, HEIGHT, this);
                g.setColor(Color.WHITE);
                g.setFont(gameFont.deriveFont(Font.PLAIN, 150f * percentageWidth));
                drawCenteredString("Ready Players", WIDTH, HEIGHT / 2, g);
                g.setFont(gameFont.deriveFont(Font.PLAIN, 30f * percentageWidth));
                drawCenteredString("Press 'E' to continue", WIDTH, (int) (HEIGHT / 2 + 350 * percentageHeight), g);
            }
        else if (part == PART.RDYP2)
            drawStartingText(g, 2);

        else if (part == PART.GOP1 || part == PART.GOP2) {
            g.drawImage(menuBackground, 0, 0, WIDTH, HEIGHT, this);
            g.setFont(gameFont.deriveFont(Font.BOLD, 190f * percentageWidth));
            g.setColor(Color.WHITE);
            drawCenteredString("GAME OVER", WIDTH, HEIGHT / 2, g);
            g.setFont(gameFont.deriveFont(Font.BOLD, 30f * percentageWidth));
            String temp;
            temp = "Press 'E' to continue";
            drawCenteredString(temp, WIDTH, (int) (HEIGHT / 2 + 350 * percentageHeight), g);
            if (gamemode != GAMEMODE.INFINITE)
                temp = "You died at level " + getLevel();
            else
                temp = "You died";
            drawCenteredString(temp, WIDTH, (int) (HEIGHT + 140 * percentageHeight), g);
            temp = "Stats:";
            g.drawString(temp, WIDTH / 2 - 150, (int) (HEIGHT / 2 + 150 * percentageHeight));
            temp = "- Kills: " + getTotalKills();
            g.drawString(temp, WIDTH / 2 - 150, (int) (HEIGHT / 2 + 190 * percentageHeight));
            temp = "- Score: " + getScore();
            g.drawString(temp, WIDTH / 2 - 150, (int) (HEIGHT / 2 + 230 * percentageHeight));
            temp = "- Time spent: " + TimeUnit.MINUTES.convert(getTimeTaken(), TimeUnit.MILLISECONDS) + " mins " + TimeUnit.SECONDS.convert(getTimeTaken(), TimeUnit.MILLISECONDS) % 60+ " secs";
            g.drawString(temp, WIDTH / 2 - 150, (int) (HEIGHT / 2 + 270 * percentageHeight));
            if (gamemode == GAMEMODE.TIMED)
            {
                temp = "- Best time: " + TimeUnit.MINUTES.convert(getHighscore(), TimeUnit.MILLISECONDS) + " mins " + TimeUnit.SECONDS.convert(getHighscore(), TimeUnit.MILLISECONDS) % 60 + " secs";
            }
            else
            {
                temp = "- High score: " + getHighscore();
            }
            g.drawString(temp, WIDTH / 2 - 150, (int) (HEIGHT / 2 + 310 * percentageHeight));

        }

        else if (part == PART.WIN && gamemode == GAMEMODE.TIMED) {
            g.drawImage(menuBackground, 0, 0, WIDTH, HEIGHT, this);
            g.setFont(gameFont.deriveFont(Font.BOLD, 190f * percentageWidth));
            g.setColor(Color.WHITE);
            drawCenteredString("YOU WIN", WIDTH, HEIGHT / 2, g);
            g.setFont(gameFont.deriveFont(Font.BOLD, 30f * percentageWidth));
            String temp;
            temp = "Press 'E' to continue";
            drawCenteredString(temp, WIDTH, (int) (HEIGHT / 2 + 350 * percentageHeight), g);
            temp = "Stats:";
            g.drawString(temp, WIDTH / 2 - 150, (int) (HEIGHT / 2 + 150 * percentageHeight));
            temp = "- Kills: " + getTotalKills();
            g.drawString(temp, WIDTH / 2 - 150, (int) (HEIGHT / 2 + 190 * percentageHeight));
            temp = "- Time spent: " + TimeUnit.MINUTES.convert(getTimeTaken(), TimeUnit.MILLISECONDS) + " mins " + TimeUnit.SECONDS.convert(getTimeTaken(), TimeUnit.MILLISECONDS) % 60 + " secs";
            g.drawString(temp, WIDTH / 2 - 150, (int) (HEIGHT / 2 + 230 * percentageHeight));
            temp = "- Best time: " + TimeUnit.MINUTES.convert(getHighscore(), TimeUnit.MILLISECONDS) + " mins " + TimeUnit.SECONDS.convert(getHighscore(), TimeUnit.MILLISECONDS) % 60 + " secs";
            g.drawString(temp, WIDTH / 2 - 150, (int) (HEIGHT / 2 + 270 * percentageHeight));
        }

        else if (part == PART.WIN && gamemode == GAMEMODE.LEVELS)
        {
            g.drawImage(menuBackground, 0, 0, WIDTH, HEIGHT, this);
            g.setFont(gameFont.deriveFont(Font.BOLD, 190f * percentageWidth));
            g.setColor(Color.WHITE);
            drawCenteredString("YOU WIN", WIDTH, HEIGHT / 2, g);
            g.setFont(gameFont.deriveFont(Font.BOLD, 30f * percentageWidth));
            String temp;
            temp = "Press 'E' to continue";
            drawCenteredString(temp, WIDTH, (int) (HEIGHT / 2 + 350 * percentageHeight), g);
            temp = "Stats:";
            g.drawString(temp, WIDTH / 2 - 150, (int) (HEIGHT / 2 + 150 * percentageHeight));
            temp = "- Kills: " + getTotalKills();
            g.drawString(temp, WIDTH / 2 - 150, (int) (HEIGHT / 2 + 190 * percentageHeight));
            temp = "- Score: " + getScore();
            g.drawString(temp, WIDTH / 2 - 150, (int) (HEIGHT / 2 + 230 * percentageHeight));
            temp = "- Time spent: " + getTimeTaken() + " minutes";
            g.drawString(temp, WIDTH / 2 - 150, (int) (HEIGHT / 2 + 270 * percentageHeight));
            temp = "- High score: " + getHighscore();
            g.drawString(temp, WIDTH / 2 - 150, (int) (HEIGHT / 2 + 310 * percentageHeight));
        }

        ////////////////////////////////////

        g.dispose();
        this.getBufferStrategy().show();
    }

    public void keyPressed(KeyEvent e) {
        
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_F12)
            System.exit(0);

        if ((part == PART.P1 || part == PART.P2)) {
            if (key == KeyEvent.VK_D) { // player 1 right
                if (p1.getPowerUps().contains(PowerUp.POWER_UP_TYPE.INCREASED_SPEED))
                    p1.setVelX(2.5 * 1.35 * percentageWidth);
                else
                    p1.setVelX(2.5 * percentageWidth);
            }
            if (key == KeyEvent.VK_A) { // player 1 left
                if (p1.getPowerUps().contains(PowerUp.POWER_UP_TYPE.INCREASED_SPEED))
                    p1.setVelX(-2.5 * 1.35 * percentageWidth);
                else
                    p1.setVelX(-2.5 * percentageWidth);
            }
            if (key == KeyEvent.VK_S) { // player 1 down
                if (p1.getPowerUps().contains(PowerUp.POWER_UP_TYPE.INCREASED_SPEED))
                    p1.setVelY(2.5 * 1.35 * percentageWidth);
                else
                    p1.setVelY(2.5 * percentageWidth);
            }
            if (key == KeyEvent.VK_W) { // player 1 up
                if (p1.getPowerUps().contains(PowerUp.POWER_UP_TYPE.INCREASED_SPEED))
                    p1.setVelY(-2.5 * 1.35 * percentageWidth);
                else
                    p1.setVelY(-2.5 * percentageWidth);
            }

            if (p2 != null) {
                if (key == KeyEvent.VK_RIGHT) { // player 2 right
                    if (p2.getPowerUps().contains(PowerUp.POWER_UP_TYPE.INCREASED_SPEED))
                        p2.setVelX(2.5 * 1.35 * percentageWidth);
                    else
                        p2.setVelX(2.5 * percentageWidth);
                }
                if (key == KeyEvent.VK_LEFT) { // player 2 left
                    if (p2.getPowerUps().contains(PowerUp.POWER_UP_TYPE.INCREASED_SPEED))
                        p2.setVelX(-2.5 * 1.35 * percentageWidth);
                    else
                        p2.setVelX(-2.5 * percentageWidth);
                }
                if (key == KeyEvent.VK_DOWN) { // player 2 down
                    if (p2.getPowerUps().contains(PowerUp.POWER_UP_TYPE.INCREASED_SPEED))
                        p2.setVelY(2.5 * 1.35 * percentageWidth);
                    else
                        p2.setVelY(2.5 * percentageWidth);
                }
                if (key == KeyEvent.VK_UP) { // player 2 up
                    if (p2.getPowerUps().contains(PowerUp.POWER_UP_TYPE.INCREASED_SPEED))
                        p2.setVelY(-2.5 * 1.35 * percentageWidth);
                    else
                        p2.setVelY(-2.5 * percentageWidth);
                }
            }
        }

        // player 1 shoot
        if (key == KeyEvent.VK_SPACE && (part == PART.P1 || part == PART.P2)) {
            if (ea.contains(p1)) {
                p1.shoot();
            }
        }

        // player 2 shoot
        if ((key == KeyEvent.VK_NUMPAD0 || key == KeyEvent.VK_INSERT) && (part == PART.P1 || part == PART.P2)) {
            if (ea.contains(p2)) {
                p2.shoot();
            }
        }

        if (key == KeyEvent.VK_P)
            paused = !paused;
    }



    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (part == PART.P1 || part == PART.P2) {
            if (key == KeyEvent.VK_D && ea.contains(p1)) { // player 1 right
                p1.setVelX(0);
            }
            if (key == KeyEvent.VK_A  &&  ea.contains(p1)) { // player 1 left
                p1.setVelX(0);
            }
            if (key == KeyEvent.VK_S  &&  ea.contains(p1)) { // player 1 down
                p1.setVelY(0);
            }
            if (key == KeyEvent.VK_W  &&  ea.contains(p1)) { // player 1 up
                p1.setVelY(0);
            }
            if (key == KeyEvent.VK_RIGHT  &&  ea.contains(p2)) { // player 2 right
                p2.setVelX(0);
            }
            if (key == KeyEvent.VK_LEFT  &&  ea.contains(p2)) { // player 2 left
                p2.setVelX(0);
            }
            if (key == KeyEvent.VK_DOWN  &&  ea.contains(p2)) { // player 2 down
                p2.setVelY(0);
            }
            if (key == KeyEvent.VK_UP  &&  ea.contains(p2)) { // player 2 up
                p2.setVelY(0);
            }
        }

        if (key == KeyEvent.VK_E) {
            if (part == PART.RDYP2) {
                part = PART.P2;
                startGame();
                startTime = System.currentTimeMillis();
            }
            if (againstFriend) {
                if (part == PART.GOP1)
                    part = PART.RDYP2;
                else if (part == PART.GOP2)
                    part = PART.MENU;
            }
            else if (part == PART.GOP1) {
                part = PART.MENU;
            }
            if (part == PART.RDYP1) {
                part = PART.P1;
                startTime = System.currentTimeMillis();
            }
            if (part == PART.WIN)
                part = PART.MENU;
        }


    }

    public static void main(String[] args) throws IOException {
        Game game = new Game();

        game.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        game.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        game.setMinimumSize(new Dimension(WIDTH, HEIGHT));

        final Image icon = ImageIO.read(new File("res\\icon.png"));

        game.frame = new JFrame(Game.TITLE);

        game.panel = new JPanel();

        game.panel.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        game.panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        game.panel.setMaximumSize(new Dimension(WIDTH, HEIGHT));

        game.panel.setLocation(0, 0);

        game.panel.setLayout(new GridLayout(1, 1));

        game.panel.add(game);

        game.panel.setOpaque(false);
        game.panel.setBackground(new Color(0, 0, 0));
        game.panel.setBorder(null);
        game.panel.setDoubleBuffered(true);

        game.helpTextArea = new JTextArea();

        game.frame.add(game.helpTextArea);

        game.frame.add(game.panel);
        game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.frame.setIconImage(icon);
        game.frame.setResizable(false);
        game.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        game.frame.setUndecorated(true);
        game.frame.pack();
        game.frame.setVisible(true);
        game.helpTextArea.setVisible(false);

        game.start();
    }

    private void playBulletSound() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        try {
            if (PLAY_SFX)
            {
                Clip tempAttack = ATTACK_1;

                tempAttack.setFramePosition(0);
                tempAttack.start();
            }
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }

    }

    public BufferedImage getSpriteSheet() {
        return spriteSheet;
    }

    public int getEnemyCount() {
        return enemyCount;
    }

    public void setEnemyCount(int enemyCount) {
        this.enemyCount = enemyCount;
    }

    public int getEnemyKilled() {
        return enemyKilled;
    }

    public void setEnemyKilled(int enemyKilled) {
        this.enemyKilled = enemyKilled;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTotalKills() {
        return totalKills;
    }

    public void setTotalKills(int totalKills) {
        this.totalKills = totalKills;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    private void setTimeTaken(long timeTaken) {
        this.timeTaken = timeTaken;
    }

    public long getTimeTaken() {
        return timeTaken;
    }

    public long getHighscore() {
        return highScore;
    }

    public void setHighScore(Long highScore) {
        this.highScore = highScore;
    }

    public void startGame() {

        if (withFriend) {
            p1 = new Player(t, c, (float) 150 * percentageWidth, HEIGHT - 150 * percentageHeight, this, t.player1);
            ea.add(p1);
            p2 = new Player(t, c, (float) WIDTH - 150 * percentageWidth, HEIGHT - 150 * percentageHeight, this , t.player2);
            ea.add(p2);
        }
        else if (againstFriend && part == PART.P2) {
            p1 = new Player(t, c, (float) WIDTH / 2 - 16 * percentageWidth, HEIGHT - 150 * percentageHeight, this, t.player2);
            ea.add(p1);
        }
        else
        {
            p1 = new Player(t, c, (float) WIDTH / 2 - 16 * percentageWidth, HEIGHT - 150 * percentageHeight, this, t.player1);
            ea.add(p1);
        }

        if (gamemode == GAMEMODE.LEVELS) {
            setLevel(1);
            setEnemyCount(4 + getLevel());
            setEnemyKilled(0);
            p1.setLives(4);
            setTotalKills(0);
            setScore(0);
            c.createEnemy(getEnemyCount());
        }
        else if (gamemode == GAMEMODE.INFINITE) {
            setLevel(0);
            setEnemyCount(6);
            setEnemyKilled(0);
            p1.setLives(4);
            setTotalKills(0);
            setScore(0);
            c.createEnemy(getEnemyCount());
        }
        else if (gamemode == GAMEMODE.TIMED) {
            setLevel(1);
            setEnemyCount(4 + getLevel());
            setEnemyKilled(0);
            p1.setLives(4);
            setTotalKills(0);
            setScore(0);
            c.createEnemy(getEnemyCount());
        }
    }

    public void endGame() {
        for (int i = 0; i < ea.size(); i++) {
            try {
                while (ea.contains(ea.get(i))) {
                    c.removeEntity(ea.get(i));
                }
            }
            catch (Exception ex) {
                System.out.println("Entity A deletion Error: " + ex.getMessage());
            }
        }

        for (int i = 0; i < eb.size(); i++) {
            try {
                while (eb.contains(eb.get(i))) {
                    c.removeEntity(eb.get(i));
                }
            }
            catch (Exception ex) {
                System.out.println("Entity B Deletion Error: " + ex.getMessage());
            }
        }

        for (int i = 0; i < ec.size(); i++) {
            try {
                while (ec.contains(ec.get(i))) {
                    c.removeEntity(ec.get(i));
                }
            }
            catch (Exception ex) {
                System.out.println("Entity C Deletion Error: " + ex.getMessage());
            }
        }

        for (int i = 0; i < ed.size(); i++) {
            try {
                while (ed.contains(ed.get(i))) {
                    c.removeEntity(ed.get(i));
                }
            }
            catch (Exception ex) {
                System.out.println("Entity D Deletion Error: " + ex.getMessage());
            }
        }

        if (dead == 0)
            part = PART.GOP1;
        else if (dead == 1 && againstFriend)
            part = PART.GOP2;
        else
            part = PART.MENU;

        setTimeTaken(System.currentTimeMillis() - startTime);
    }

    public void resetGame() {
        endGame();
        startGame();
    }

    public void drawCenteredString(String s, int w, int h, Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        int x = (w - fm.stringWidth(s)) / 2;
        int y = (fm.getAscent() + (h - fm.getAscent() + (fm.getDescent())) / 2);
        g.drawString(s, x, y);
    }

    private void drawStartingText(Graphics g, int player) {
        if (player > 2 && player < 1)
            throw new IllegalArgumentException("Invalid Input: Player can only be 1 or 2");
        g.drawImage(menuBackground, 0, 0, WIDTH, HEIGHT, this);
        g.setColor(Color.WHITE);
        g.setFont(gameFont.deriveFont(Font.PLAIN, 150f * percentageWidth));
        if (player == 1)
            drawCenteredString("Ready Player One", WIDTH, HEIGHT / 2, g);
        if (player == 2)
            drawCenteredString("Ready Player Two", WIDTH, HEIGHT / 2, g);
        g.setFont(gameFont.deriveFont(Font.PLAIN, 30f * percentageWidth));
        drawCenteredString("Press 'E' to continue", WIDTH, (int) (HEIGHT / 2 + 350 * percentageHeight), g);
    }

    public void drawStringInRectangle(String string, Rectangle rectangle, boolean resizeRectangle, Graphics g) {
        FontRenderContext frc = g.getFontMetrics().getFontRenderContext();
        FontMetrics fm = g.getFontMetrics();
        int textWidth = (int) g.getFont().getStringBounds(string, frc).getWidth();
        int textHeight = fm.getHeight() + fm.getDescent();
        int stringX, stringY;
        if (resizeRectangle) {
            rectangle.setBounds(new Rectangle((int) rectangle.getX(), (int) rectangle.getY(), textWidth + 50, textHeight + 50));
            int rectWidth = (int) rectangle.getWidth();
            int rectHeight = (int) rectangle.getHeight();
            stringX = (int) (rectangle.getX() - 25 + (rectWidth - textWidth));
            stringY = (int) (rectangle.getY() + (rectHeight - 25 + textHeight) / 2 - fm.getDescent());
        }
        else {
            int rectWidth = (int) rectangle.getWidth();
            int rectHeight = (int) rectangle.getHeight();
            stringX = ((int) rectangle.getX() + rectWidth) - textWidth - ((rectWidth - textWidth)/ 2);
            stringY = (int) rectangle.getY() + ((rectHeight - fm.getHeight()) / 2) + fm.getAscent();
        }

        g.drawString(string, stringX, stringY);
    }

    public void saveHighscoreAndStopGame(File file) {
        try {

            boolean fileExists = file.exists();

            if (!fileExists) {
                fileExists = file.createNewFile();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
                if (gamemode == GAMEMODE.TIMED)
                    writer.write("0");
                else
                    writer.write("0");
                writer.flush();
                writer.close();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            long tempHighScore = Long.parseLong(reader.readLine());

            setHighScore(tempHighScore);
            setTimeTaken(System.currentTimeMillis() - startTime);

            if (getScore() > tempHighScore || (getTimeTaken() < tempHighScore) || tempHighScore == 0) {
                if (Game.gamemode == Game.GAMEMODE.TIMED) {
                    setHighScore(getTimeTaken());
                }
                else {
                    setHighScore((long) getScore());
                }
                File temp = File.createTempFile("temp", ".txt", new File("res\\"));
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(temp), StandardCharsets.UTF_8));
                writer.write(String.valueOf(getHighscore()));

                writer.flush();
                writer.close();
                reader.close();

                Files.delete(file.toPath());
                boolean renamed = temp.renameTo(file.getAbsoluteFile());
            }

            dead++;
            deathMessageWait = true;

            if (Game.againstFriend && dead == 2)
                dead = 0;

            else if (dead == 1 && !Game.againstFriend)
                dead = 0;
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void playSound(String path) {
        final Thread thread = new Thread(() -> {
           try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("res/" + path).getAbsoluteFile());
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
           } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
               e.printStackTrace();
           }
        });

        thread.start();
    }
}
