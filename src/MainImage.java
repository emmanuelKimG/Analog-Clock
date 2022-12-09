import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class MainImage extends JFrame {

    private Image fondo;
    private int changeMinute;
    private Clip audioClip = null;
    AudioInputStream audio = null;

    MainImage(){
        super();
        setSize(800,800);
        setResizable(false);
        setBackground(Color.white);
        setAudio();
        changeMinute = Calendar.getInstance().get(Calendar.MINUTE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        if(fondo == null){
            var ImageClock = drawClock();
            g.drawImage(ImageClock,0,0,this);
        }
    }

    @Override
    public void update(Graphics g) {
        Calendar calendar = Calendar.getInstance();

        g.setClip(0,0,getWidth(),getHeight());

        int second = (calendar.get(Calendar.SECOND) * 6) - 90;
        int minute = (calendar.get(Calendar.MINUTE)*6) - 90;
        int hour = (calendar.get(Calendar.HOUR)*30) - 90;
        int min = calendar.get(Calendar.MINUTE);

        if(min != changeMinute && fondo != null){
            changeColor();
            changeMinute = min;
        }

        Image buffer = createImage(getWidth(), getHeight());

        Graphics gbuffer =  buffer.getGraphics();
        gbuffer.setClip(0,0,getWidth(),getHeight());
        gbuffer.drawImage(fondo,0,0,this);

        drawMinute(minute,gbuffer);
        drawHour(hour,gbuffer);
        drawSecond(second,gbuffer);

        g.drawImage(buffer,0,0,this);

    }


    public Image drawClock(){

        fondo = createImage(getWidth(),getHeight());
        Graphics gFondo = fondo.getGraphics();
        gFondo.setClip(0,0,getWidth(),getHeight());
        gFondo.setColor(Color.BLACK);
        gFondo.fillOval((getWidth()/2)-250,(getHeight()/2)-250,500,500);
        gFondo.setColor(Color.WHITE);
        for(int i =0 ; i < 60;){
            gFondo.fillArc((getWidth()/2)-240,(getHeight()/2)-240, 480,480,i*6,1);
            i+=5;
        }
        gFondo.setColor(Color.black);
        gFondo.fillArc((getWidth()/2)-200,(getHeight()/2)-200,400,400,0,360);

        return fondo;
    }

    public void changeColor(){
        Color randomColor = new Color((int) (Math.random()*255), (int) (Math.random()*255), (int) (Math.random()*255));
        if(fondo != null){
            fondo = createImage(getWidth(),getHeight());
            Graphics gFondo = fondo.getGraphics();
            gFondo.setClip(0,0,getWidth(),getHeight());
            gFondo.setColor(randomColor);
            gFondo.fillOval((getWidth()/2)-275,(getHeight()/2)-275,550,550);
            gFondo.setColor(Color.BLACK);
            gFondo.fillOval((getWidth()/2)-250,(getHeight()/2)-250,500,500);
            gFondo.setColor(randomColor);

            for(int i =0 ; i < 60;){
                gFondo.fillArc((getWidth()/2)-240,(getHeight()/2)-240, 480,480,i*6,1);
                i+=5;
            }
            gFondo.setColor(Color.black);
            gFondo.fillArc((getWidth()/2)-200,(getHeight()/2)-200,400,400,0,360);
        }
    }

    public void setAudio(){
        try {
            audio = AudioSystem.getAudioInputStream(new File("src/clock.wav"));
            audioClip = AudioSystem.getClip();
            audioClip.open(audio);
        } catch (UnsupportedAudioFileException |IOException | LineUnavailableException e ) {
            e.printStackTrace();
        }
    }

    public void drawHour(int angle,Graphics g){
        g.setColor(Color.cyan);
        g.fillArc((getWidth()/2)-225,(getHeight()/2)-225, 450,450,-angle,1);
    }

    public void drawMinute(int angle , Graphics g){
        audioClip.loop(6);
        g.setColor(Color.YELLOW);
        g.fillArc((getWidth()/2)-150,(getHeight()/2)-150, 300,300,-angle,1);
    }

    public void drawSecond(int angle,Graphics g){
        g.setColor(Color.green);
        g.fillArc((getWidth()/2)-200,(getHeight()/2)-200, 400,400,-angle,1);

    }

    public static void main(String[] args) {
        Runnable hilo = () -> {
            MainImage reloj = new MainImage();
            while(true) {
                try {
                    reloj.update(reloj.getGraphics());
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread runThread = new Thread(hilo);
        runThread.start();
    }
}
