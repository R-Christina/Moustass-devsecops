package uinterface;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import calcul.Calcul;

public class Uinterface extends JFrame 
{
    public JTextField champ_a = new JTextField(10);
    public JTextField champ_b = new JTextField(10);
    public JTextField champ_c = new JTextField(10);
    public JLabel message_user = new JLabel("résultat :");

//----------------------------------------------------------------------------
    //window propriety
    public Uinterface() 
    {
        super("Calcul d'équation de second degré");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        message_user.setHorizontalAlignment(SwingConstants.CENTER);
        add(message_user, BorderLayout.NORTH);
        // add(message_user, BorderLayout.SOUTH);


        //3 champs : a, b, c
        JPanel center = new JPanel(new GridLayout(3, 2, 6, 6));
        center.add(new JLabel("a :"));
        center.add(champ_a);
        center.add(new JLabel("b :"));
        center.add(champ_b);
        center.add(new JLabel("c :"));
        center.add(champ_c);
        add(center, BorderLayout.CENTER);

        //bouton action
        JButton btn = new JButton("Calculer");
        btn.addActionListener(this::resultSecondDegre);
        JPanel south = new JPanel();
        south.add(btn);
        add(south, BorderLayout.SOUTH);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

       // taille en petite
       //window au milieu
        // pack();
        // setLocationRelativeTo(null);
        // setVisible(true);

    }
//----------------------------------------------------------------------------

//FUNCTION
// ------------------------------------------------------------------------------
    //Function calculer operation delta second degre
    public void resultSecondDegre(ActionEvent e) 
    {
        try 
        {
            int a = parseInt(champ_a.getText());
            int b = parseInt(champ_b.getText());
            int c = parseInt(champ_c.getText());

            Calcul calcul = new Calcul(a, b, c);
            double[] resultat = calcul.calculSeconddegre();

            if (resultat == null) 
            {
                message_user.setText("aucune solution (delta < 0)");
            } 
            else if (resultat.length == 1) 
            {
                message_user.setText("une seule solution : x = " + resultat[0]);
            } 
            else 
            {
                message_user.setText("deux solutions : x1 = " + resultat[0] + " , x2 = " + resultat[1]);
            }

        } 
        catch (NumberFormatException ex) 
        {
            message_user.setText("Mauvaise saisie, entrez des nombres valides");
        }
    }
        
// -----------------------------------------------------------------------------

    // Function Parse text -> chiffre

    public Integer parseInt(String text) 
    {
        if (text == null) throw new NumberFormatException("null");
        String norm = text.trim().replace(',', '.');
        if (norm.isEmpty()) throw new NumberFormatException("null");
        return Integer.parseInt(norm);
    }


//======================================================
    //Main
    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(Uinterface::new);
    }
}
