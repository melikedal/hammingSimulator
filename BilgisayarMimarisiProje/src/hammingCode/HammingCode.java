 package hammingCode;

import java.awt.Color;
import java.awt.Font;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

 class HammingGUI extends JFrame{
	 
	 JTextPane kodGirdiAlani;
	 JTextArea kodCiktiAlani;
	 
	  JTextField girdiAlani;
      JTextArea ciktiAlani;
	
	

	HammingGUI(String baslik){
		super(baslik);
		this.setSize(800,650);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(null);
		
		
		JTextField bitBozAlani = new JTextField();
		bitBozAlani.setBounds(180, 550, 100, 25);
		this.add(bitBozAlani);

		
		kodGirdiAlani=new JTextPane();
		JScrollPane girisEkraniKaydirma=new JScrollPane(kodGirdiAlani);
		
		girisEkraniKaydirma.setBounds(20, 20, 740, 200);
		
		 Font girisBaslikFont = new Font("Courier New", Font.BOLD, 14);
		 
		TitledBorder baslikTasarim = BorderFactory.createTitledBorder("Kod Giriniz -->(0 VEYA 1)");
		baslikTasarim.setTitleJustification(TitledBorder.CENTER);  
		baslikTasarim.setTitleFont(girisBaslikFont);                   
		baslikTasarim.setTitleColor(Color.black);  
		
		kodGirdiAlani.setBorder(baslikTasarim);

        this.add(girisEkraniKaydirma);
        
        JButton bitBozButonu = new JButton("Bit Boz");
        bitBozButonu.setBounds(300, 550, 100, 25);
        this.add(bitBozButonu);

        JButton hataTespitButonu = new JButton("Hata Tespit Et");
        hataTespitButonu.setBounds(410, 550, 140, 25);
        this.add(hataTespitButonu);

        JButton duzeltButonu = new JButton("Düzelt");
        duzeltButonu.setBounds(560, 550, 100, 25);
        this.add(duzeltButonu);

        
        final int[][] mevcutKod = new int[1][];
        
        JButton kodUretButonu = new JButton("Hamming Kodunu Üret");
        kodUretButonu.setBounds(300, 230, 200, 30);
        this.add(kodUretButonu);
        
        
        Map<Integer, int[]> bellek = new HashMap<>();
	    final int[] adresSayaci = {0}; 
        
        
        JButton bellegeYazButon = new JButton("Belleğe Yaz");
        bellegeYazButon.setBounds(20, 510, 140, 25);
	    this.add(bellegeYazButon);

	    JTextField adresAlani = new JTextField();
	    adresAlani.setBounds(180, 510, 100, 25);
	    this.add(adresAlani);

	    JButton bellektenOkuButon = new JButton("Bellekten Oku");
	    bellektenOkuButon.setBounds(300, 510, 140, 25);
	    this.add(bellektenOkuButon);
        
	   
        
        kodUretButonu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String kodGirisi = kodGirdiAlani.getText().trim();
                
                if (!kodGirisi.matches("[01]+") || !(kodGirisi.length() == 8 || kodGirisi.length() == 16 || kodGirisi.length() == 32)) {
                    JOptionPane.showMessageDialog(null, "Lütfen 8, 16 veya 32 bitlik sadece 0 ve 1'lerden oluşan bir veri giriniz.");
                    return;
                }

                int[] dataBitleri = new int[kodGirisi.length()];
                for (int i = 0; i < kodGirisi.length(); i++) {
                    dataBitleri[i] = kodGirisi.charAt(i) - '0';
                }

                int[] hammingCode = HammingLogic.hammingKodOlustur(dataBitleri);

                StringBuilder sb = new StringBuilder();
                sb.append("Hamming Kodu (SEC-DED):\n\n");
                for (int i = 1; i < hammingCode.length; i++) {
                    sb.append(hammingCode[i]);
                }

                kodCiktiAlani.setText(sb.toString());
                mevcutKod[0] = hammingCode.clone(); 
            }
        });
        
        bitBozButonu.addActionListener(e -> {
            try {
                int bitKonumu = Integer.parseInt(bitBozAlani.getText().trim());
                HammingLogic.bitiTersCevir(mevcutKod[0], bitKonumu);
                kodCiktiAlani.setText("Bit " + bitKonumu + " bozuldu!\n\nKod:\n" + HammingLogic.metinDöndürKod(mevcutKod[0]));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Lütfen geçerli bir bit konumu girin!");
            }
        });

        hataTespitButonu.addActionListener(e -> {
            int pos = HammingLogic.hataPozisyonuBul(mevcutKod[0]);
            if (pos == 0) {
                JOptionPane.showMessageDialog(null, "Hata tespit edilmedi. Kod doğru.");
            } else if (pos == -1) {
                JOptionPane.showMessageDialog(null, "Çift hata tespit edildi. Düzeltilemez.");
            } else {
                JOptionPane.showMessageDialog(null, "Hatalı bit pozisyonu: " + pos);
            }
        });
       
        duzeltButonu.addActionListener(e -> {
            int pos = HammingLogic.hataPozisyonuBul(mevcutKod[0]);
            if (pos > 0) {
                HammingLogic.hataliBitiDüzelt(mevcutKod[0], pos);
                kodCiktiAlani.setText("Hata düzeltildi! Yeni Kod:\n\n" + HammingLogic.metinDöndürKod(mevcutKod[0]));
            } else if (pos == 0) {
                JOptionPane.showMessageDialog(null, "Kod zaten doğru.");
            } else {
                JOptionPane.showMessageDialog(null, "Çift hata var. Düzeltilemez.");
            }
        });
        
        bellegeYazButon.addActionListener(e -> {
	        if (mevcutKod[0] != null) {
	            bellek.put(adresSayaci[0], mevcutKod[0].clone());
	            JOptionPane.showMessageDialog(null, "Belleğe yazıldı. Adres: " + adresSayaci[0]);
	            adresSayaci[0]++;
	        } else {
	            JOptionPane.showMessageDialog(null, "Önce kod üretmelisiniz!");
	        }
	    });
        
        
        bellektenOkuButon.addActionListener(e -> {
            try {
                int adres = Integer.parseInt(adresAlani.getText().trim());
                if (bellek.containsKey(adres)) {
                    mevcutKod[0] = bellek.get(adres).clone();
                    kodCiktiAlani.setText("Adres " + adres + "'ten okundu:\n\n" + HammingLogic.metinDöndürKod(mevcutKod[0]));
                } else {
                    JOptionPane.showMessageDialog(null, "Bu adreste veri bulunamadı.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Geçerli bir adres giriniz.");
            }
        });


        kodCiktiAlani=new JTextArea();
        kodCiktiAlani.setEditable(false);
        JScrollPane cikisEkraniKaydirma = new JScrollPane(kodCiktiAlani);
        cikisEkraniKaydirma.setBounds(20, 280, 740, 250);
       
      
       
       Font cikisBaslikFont = new Font("Courier New", Font.BOLD, 14);
       
       TitledBorder baslikTasarim2 = BorderFactory.createTitledBorder("Hamming Kod Çıktısı");
		baslikTasarim2.setTitleJustification(TitledBorder.CENTER);  
		baslikTasarim2.setTitleFont(cikisBaslikFont);                    
		baslikTasarim2.setTitleColor(Color.black);                   
	        
		kodCiktiAlani.setBorder(baslikTasarim2);

        this.add(cikisEkraniKaydirma);
		this.setVisible(true);
	
}
}
 
 class HammingLogic {

	   
	    public static int parityBitHesapla(int dataUzunlugu) {
	        int r = 0;
	        while (Math.pow(2, r) < (dataUzunlugu + r + 1)) {
	            r++;
	        }
	        return r;
	    }

	    
	    public static int[] hammingKodOlustur(int[] dataBitleri) {
	        int m = dataBitleri.length;
	        int r = parityBitHesapla(m);
	        int totalBitler = m + r + 1; 

	        int[] kodSözcüğü = new int[totalBitler + 1]; 

	        int dataIndex = 0;
	        for (int i = 1; i <= totalBitler; i++) {
	            if ((i & (i - 1)) == 0 || i == totalBitler) {
	            	kodSözcüğü[i] = 0; 
	            } else {
	            	kodSözcüğü[i] = dataBitleri[dataIndex++];
	            }
	        }

	        
	        for (int i = 1; i <= totalBitler; i <<= 1) {
	            int parity = 0;
	            for (int j = 1; j <= totalBitler; j++) {
	                if (((j & i) != 0) && j != i) {
	                    parity ^= kodSözcüğü[j];
	                }
	            }
	            kodSözcüğü[i] = parity;
	        }

	       
	        int genelParity = 0;
	        for (int i = 1; i < totalBitler; i++) {
	            genelParity ^= kodSözcüğü[i];
	        }
	        kodSözcüğü[totalBitler] = genelParity;

	        return kodSözcüğü;
	    }
	    
	    
	    public static void bitiTersCevir(int[] kodSözcüğü, int bitKonumu) {
	        if (bitKonumu > 0 && bitKonumu < kodSözcüğü.length) {
	            kodSözcüğü[bitKonumu] ^= 1; 
	        }
	    }

	    
	    public static int hataPozisyonuBul(int[] kodSözcüğü) {
	        int hataKonumu = 0;
	        int totalBit = kodSözcüğü.length - 1;

	        for (int i = 1; i <= totalBit; i <<= 1) {
	            int parity = 0;
	            for (int j = 1; j <= totalBit; j++) {
	                if (((j & i) != 0) && j != i) {
	                    parity ^= kodSözcüğü[j];
	                }
	            }
	            if (parity != kodSözcüğü[i]) {
	                hataKonumu += i;
	            }
	        }

	        
	        int genelParity = 0;
	        for (int i = 1; i <= totalBit; i++) {
	            genelParity ^= kodSözcüğü[i];
	        }

	        if (genelParity != kodSözcüğü[totalBit] && hataKonumu == 0) {
	            return -1; 
	        }

	        return hataKonumu; 
	    }

	    
	    public static void hataliBitiDüzelt(int[] kodSözcüğü, int bitKonumu) {
	        if (bitKonumu > 0 && bitKonumu < kodSözcüğü.length) {
	        	kodSözcüğü[bitKonumu] ^= 1;
	        }
	    }

	    
	    public static String metinDöndürKod(int[] kodSözcüğü) {
	        StringBuilder sb = new StringBuilder();
	        for (int i = 1; i < kodSözcüğü.length; i++) {
	            sb.append(kodSözcüğü[i]);
	        }
	        return sb.toString();
	    }
	    
        

	}
public class HammingCode {

    public static void main(String[] args) {
        
    	 new HammingGUI("Hamming SEC-DED Simülatörü");
    }
}

