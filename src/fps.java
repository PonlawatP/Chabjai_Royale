import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferStrategy;

    /**
     * Base code for active rendering
     */
    public class fps extends JFrame implements Runnable
    {

        private BufferStrategy buffer;
        private boolean running = true;

        public fps()
        {
            super("My Game");
            setSize(640, 480);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setVisible(true);
            // Create 2 buffers
            createBufferStrategy(2);
            buffer = getBufferStrategy();

            // Run game loop
            new Thread(this).run();
        }

        /**
         * The Game Loop
         */
        public void run()
        {
            long now = getCurrentTime();
            long gameTime = getCurrentTime();

            long frames = 0;
            long fps = 0;

            long lastFpsCount = getCurrentTime();

            long updateRate = 1000 / 60; // Update 60 times per second

            while (running)
            {
                now = getCurrentTime();

                while (now + updateRate > gameTime)
                {
                    // update your logic here
                    update();
                    gameTime += updateRate;
                    // render
                    do
                    {
                        do
                        {
                            Graphics g = buffer.getDrawGraphics();
                            render(g);
                            g.dispose();

                            frames++;
                            if (now - lastFpsCount > 1000)
                            {
                                fps = frames;
                                frames = 0;
                                lastFpsCount = getCurrentTime();
                                setTitle("FPS: " + fps);
                            }
                        }
                        while (buffer.contentsRestored());

                        buffer.show();
                    }
                    while (buffer.contentsLost());
                }
            }
        }

        /**
         * Update the game
         */
        public void update()
        {
            System.out.println("updated");
        }

        /**
         * Render the game
         */
        public void render(Graphics g)
        {
            System.out.println("rendered");
        }

        /**
         * Returns the current time in milliseconds
         */
        public long getCurrentTime()
        {
            return System.nanoTime() / 1000000;
        }

        public static void main(String[] args)
        {
            new fps();
        }

}
