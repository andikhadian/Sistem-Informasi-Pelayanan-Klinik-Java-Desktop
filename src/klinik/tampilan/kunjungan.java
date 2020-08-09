/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package klinik.tampilan;

import com.mysql.jdbc.PreparedStatement;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author andikhadian
 */
public class kunjungan extends javax.swing.JInternalFrame {

    /** Creates new form kunjungan */
    DefaultTableModel tabel = new DefaultTableModel();
    DefaultTableModel tabel2 = new DefaultTableModel();
    DefaultTableModel tabel3 = new DefaultTableModel();
    public kunjungan() {
        initComponents();
        autoNumber();
        loadData();
        disable();
        kosong();
        tanggal();
    }
    
    public void disable(){
        txtKdKunjungan.setEnabled(false);
        txtTglKunjungan.setEnabled(false);
        txtKdDokter.setEnabled(false);
        txtNmDokter.setEnabled(false);
        txtJkDokter.setEnabled(false);
        txtSpesialisDokter.setEnabled(false);
        txtKdPasien.setEnabled(false);
        txtNmPasien.setEnabled(false);
        txtJkPasien.setEnabled(false);
        txtUmurPasien.setEnabled(false);
        txtHpPasien.setEnabled(false);
        btnStruk.setEnabled(false);
    }
    
    public void kosong() {
        txtTglKunjungan.setText("");
        txtKdPasien.setText("");
        txtNmPasien.setText("");
        txtJkPasien.setText("");
        txtUmurPasien.setText("");
        txtHpPasien.setText("");
        txtKeluhanPasien.setText("");
        txtKdDokter.setText("");
        txtNmDokter.setText("");
        txtJkDokter.setText("");
        txtSpesialisDokter.setText("");
        txtJkPasien.setEnabled(false);
        txtUmurPasien.setEnabled(false);
        txtHpPasien.setEnabled(false);
        btnTambah.setEnabled(true);
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
        btnStruk.setEnabled(false);
        
        autoNumber();
        tanggal();
    }
    
    private void tanggal(){
        Calendar kal = new GregorianCalendar();
        int tahun = kal.get(Calendar.YEAR);
        int bulan = kal.get(Calendar.MONTH);
        int tanggal = kal.get(Calendar.DAY_OF_MONTH);
        txtTglKunjungan.setText(tahun+"-"+(bulan+1)+"-"+tanggal);
    }
    
    private void autoNumber() {
        try {
            java.sql.Connection line_konek = (com.mysql.jdbc.Connection)klinik.koneksi.koneksi.getDB();
            java.sql.Statement line_statemen = line_konek.createStatement();
            String query_bukaTabel = "SELECT MAX(RIGHT(kd_kunjungan,4)) AS nomor FROM t_kunjungan";
            java.sql.ResultSet line_result = line_statemen.executeQuery(query_bukaTabel);
            if (line_result.first() == false) {
                txtKdKunjungan.setText("KNJ0001");
            } else {
                line_result.last();
                int no = line_result.getInt(1) + 1;
                String nomor = String.valueOf(no);
                int oto = nomor.length();
                switch (oto){
                    case 1: nomor ="000" +nomor; break;
                    case 2: nomor ="00" +nomor; break;
                    case 3: nomor ="0" +nomor; break;
                    case 4: nomor ="" +nomor; break;
                        
                }
                txtKdKunjungan.setText("KNJ" + nomor);
            }
        } catch (Exception e) {
            e.printStackTrace();//penanganan masalah
        }
    }
    
    private void loadData(){
        tabel.getDataVector().removeAllElements();
        tabel.fireTableDataChanged();
        try{
            java.sql.Connection conn = (Connection)klinik.koneksi.koneksi.getDB();
            java.sql.Statement stm = conn.createStatement();
            String querry = "SELECT kd_kunjungan, tgl_kunjungan, kd_pasien, keluhan, kd_dokter FROM t_kunjungan";
            java.sql.ResultSet rs = stm.executeQuery(querry);
            tableKunjungan.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(Exception e){
            System.out.println("Error Load Data : " + e);
        }
    }
    
    private void daftarPasien(){

       tabel2.getDataVector().removeAllElements();
        tabel2.fireTableDataChanged();
        try{
            java.sql.Connection conn = (Connection)klinik.koneksi.koneksi.getDB();
            java.sql.Statement stm = conn.createStatement();
            String querry_bukatable="SELECT kd_pasien, nama_pasien, umur, jenis_kelamin, no_hp from t_pasien";
            java.sql.ResultSet rs = stm.executeQuery(querry_bukatable);
            tableDialogPasien.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(Exception e){
            System.out.println("Errot load daftar pasien : " + e);
        }
    }
    
    private void daftarDokter(){

       tabel2.getDataVector().removeAllElements();
        tabel2.fireTableDataChanged();
        try{
            java.sql.Connection conn = (Connection)klinik.koneksi.koneksi.getDB();
            java.sql.Statement stm = conn.createStatement();
            String querry_bukatable="SELECT kd_dokter, nama_dokter, jenis_kelamin, spesialis from t_dokter";
            java.sql.ResultSet rs = stm.executeQuery(querry_bukatable);
            tableDialogDokter.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(Exception e){
            System.out.println("Errot load daftar dokter : " + e);
        }
    }
        
    private void simpan(){
        String kd_kunjungan = txtKdKunjungan.getText();
        String tgl_kunjungan = txtTglKunjungan.getText();
        String kd_pasien = txtKdPasien.getText();
        String keluhan_pasien = txtKeluhanPasien.getText();
        String kd_dokter = txtKdDokter.getText();
        String status = "Belum Rekam Medis";
        
       try{
           Connection conn =(Connection)klinik.koneksi.koneksi.getDB();
           java.sql.Statement stm = conn.createStatement();
           String querry ="INSERT INTO t_kunjungan VALUES ('"+kd_kunjungan+"','"+tgl_kunjungan+"', '"+kd_pasien+"', '"+keluhan_pasien+"', '"+kd_dokter+"', '"+status+"')";
           stm.executeUpdate(querry);
           JOptionPane.showMessageDialog(this, "Berhasil menambahkan kunjungan pasien baru", "Sukses", JOptionPane.INFORMATION_MESSAGE);
       }catch(Exception e){
           JOptionPane.showMessageDialog(this, "Gagal Memasukkan Data !", "Peringatan", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
       }
    }
    
    private void ubah(){
        try{
            String kd_kunjungan = txtKdKunjungan.getText();
            String tgl_kunjungan = txtTglKunjungan.getText();
            String kd_pasien = txtKdPasien.getText();
            String keluhan_pasien = txtKeluhanPasien.getText();
            String kd_dokter = txtKdDokter.getText();
            
            Connection conn =(Connection)klinik.koneksi.koneksi.getDB();
            String querry ="UPDATE t_kunjungan SET kd_pasien='"+kd_pasien+"', keluhan='"+keluhan_pasien+"', kd_dokter='"+kd_dokter+"' WHERE kd_kunjungan='"+kd_kunjungan+"'";
            com.mysql.jdbc.PreparedStatement prepare = (PreparedStatement)conn.prepareStatement(querry);
            prepare.execute();
            JOptionPane.showMessageDialog(this, "Berhasil mengubah data kunjungan pasien", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            btnTambah.setEnabled(true);
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Gagal Mengubah Data !", "Peringatan", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
        }
    }
    
    private void hapus(){
        int s_row = tableKunjungan.getSelectedRow();
        if(s_row==-1){
            return;
        }
        String dataDelete = (String) tableKunjungan.getValueAt(s_row,0);
        try{
            Connection conn = (Connection)klinik.koneksi.koneksi.getDB();
            String querry = "DELETE FROM t_kunjungan WHERE kd_kunjungan=?";
            java.sql.PreparedStatement stm = (PreparedStatement)conn.prepareStatement(querry);
            stm.setString(1,dataDelete);
            stm.executeUpdate();
            stm.close();
            JOptionPane.showMessageDialog(this, "Berhasil menghapus data kunjungan pasien", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            btnTambah.setEnabled(true);
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Tidak bisa menghapus data", "Hapus Data", JOptionPane.WARNING_MESSAGE);
        } finally {
            loadData();
        }
    }
    
    private void klik() {
        btnTambah.setEnabled(false);
        btnUbah.setEnabled(true);
        btnHapus.setEnabled(true);
        btnStruk.setEnabled(true);
        String getKdKunjungan = tableKunjungan.getValueAt(tableKunjungan.getSelectedRow(), 0).toString();
        String getTglKunjungan = tableKunjungan.getValueAt(tableKunjungan.getSelectedRow(), 1).toString();
        String getKdPasien = tableKunjungan.getValueAt(tableKunjungan.getSelectedRow(), 2).toString();
        String getKeluhan = tableKunjungan.getValueAt(tableKunjungan.getSelectedRow(), 3).toString();
        String getKdDokter = tableKunjungan.getValueAt(tableKunjungan.getSelectedRow(), 4).toString();
        
        String sqlPasien = "SELECT * from t_pasien WHERE kd_pasien ='"+ getKdPasien+"'";
        try{
            Connection conn = (Connection)klinik.koneksi.koneksi.getDB();
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sqlPasien);
                if(hasil.next()){
                    String nama = hasil.getString("nama_pasien"); 
                    String jk = hasil.getString("jenis_kelamin"); 
                    String umur = hasil.getString("umur"); 
                    String hp = hasil.getString("no_hp"); 
                    txtNmPasien.setText(nama);
                    txtJkPasien.setText(jk);
                    txtUmurPasien.setText(umur);
                    txtHpPasien.setText(hp);
                } else {
                    System.out.println("Error get nama pasien");
                }
            }catch(SQLException e) {
                System.out.println(e);
        } 
        
        String sqlRuangan = "SELECT * from t_dokter WHERE kd_dokter ='"+ getKdDokter +"'";
        try{
            Connection conn = (Connection)klinik.koneksi.koneksi.getDB();
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sqlRuangan);
                if(hasil.next()){
                    String namaDokter = hasil.getString("nama_dokter"); 
                    String kdDokter = hasil.getString("kd_dokter"); 
                    String jkDokter = hasil.getString("jenis_kelamin"); 
                    String spesialisDokter = hasil.getString("spesialis");             
                    txtNmDokter.setText(namaDokter);
                    txtJkDokter.setText(jkDokter);
                    txtSpesialisDokter.setText(spesialisDokter);
                } else {
                    System.out.println("Error get dokter");
                }
            }catch(SQLException e) {
                System.out.println(e);
        }  
        
        txtKdKunjungan.setText(getKdKunjungan);
        txtTglKunjungan.setText(getTglKunjungan);
        txtKdPasien.setText(getKdPasien);
        txtKeluhanPasien.setText(getKeluhan);
        txtKdDokter.setText(getKdDokter);

    }
    
    private void cetakStruk() throws SQLException {
        try {
            String KdKunjungan = txtKdKunjungan.getText();
            String KdPasien = txtKdPasien.getText();
            String KdDokter = txtKdDokter.getText();
            System.out.println(KdKunjungan + KdPasien + KdDokter);
            String namafile = "src/klinik/laporan/struk_kunjungan_pasien.jasper"
                    + "";
            HashMap hash = new HashMap();
            hash.put("kd_kunjungan", KdKunjungan);
            hash.put("kd_pasien", KdPasien);
            hash.put("kd_dokter", KdDokter);
            File file = new File(namafile);
            JasperReport jasper = (JasperReport) JRLoader.loadObject(file.getPath());
            JasperPrint print = JasperFillManager.fillReport(jasper, hash, klinik.koneksi.koneksi.getDB());
            JasperViewer.viewReport(print,false);
         }catch (JRException ex) {
            Logger.getLogger(kunjungan.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    
    private void cetak() throws SQLException {
        try {
            Date awal = tgl_awal.getDate();
            Date akhir = tgl_akhir.getDate();
//            String format_awal = date.format(awal);
//            String format_akhir = date.format(akhir);
            String namafile = "src/klinik/laporan/laporan_kunjungan_pasien.jasper"
                    + "";
            HashMap hash = new HashMap();
            hash.put("tgl_awal", awal);
            hash.put("tgl_akhir", akhir);
            File file = new File(namafile);
            JasperReport jasper = (JasperReport) JRLoader.loadObject(file.getPath());
            JasperPrint print = JasperFillManager.fillReport(jasper, hash, klinik.koneksi.koneksi.getDB());
            JasperViewer.viewReport(print,false);
         }catch (JRException ex) {
            Logger.getLogger(kunjungan.class.getName()).log(Level.SEVERE, null, ex);
         }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dialogPasien = new javax.swing.JDialog();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableDialogPasien = new javax.swing.JTable();
        btnDialogKembaliPasien = new javax.swing.JButton();
        btnDialogMasukanPasien = new javax.swing.JButton();
        dialogDokter = new javax.swing.JDialog();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableDialogDokter = new javax.swing.JTable();
        btnDialogKembaliDokter = new javax.swing.JButton();
        btnDialogMasukanDokter = new javax.swing.JButton();
        dialogTindakan = new javax.swing.JDialog();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableDialogTindakan = new javax.swing.JTable();
        btnDialogKembaliTindakan = new javax.swing.JButton();
        btnDialogMasukanTindakan = new javax.swing.JButton();
        dialogLaporan = new javax.swing.JDialog();
        jLabel3 = new javax.swing.JLabel();
        tgl_awal = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        tgl_akhir = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtKdKunjungan = new javax.swing.JTextField();
        txtTglKunjungan = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtKdDokter = new javax.swing.JTextField();
        txtNmDokter = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtJkDokter = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        btnCariPasien1 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        txtSpesialisDokter = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableKunjungan = new javax.swing.JTable();
        btnLaporan = new javax.swing.JButton();
        btnStruk = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        txtKeluhanPasien = new javax.swing.JTextField();
        txtKdPasien = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtNmPasien = new javax.swing.JTextField();
        btnCariPasien = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txtJkPasien = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtUmurPasien = new javax.swing.JTextField();
        txtHpPasien = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        btnKosong = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnTambah = new javax.swing.JButton();

        dialogPasien.setSize(new java.awt.Dimension(450, 300));

        tableDialogPasien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tableDialogPasien);

        btnDialogKembaliPasien.setText("Kembali");
        btnDialogKembaliPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDialogKembaliPasienActionPerformed(evt);
            }
        });

        btnDialogMasukanPasien.setText("Masukan");
        btnDialogMasukanPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDialogMasukanPasienActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dialogPasienLayout = new javax.swing.GroupLayout(dialogPasien.getContentPane());
        dialogPasien.getContentPane().setLayout(dialogPasienLayout);
        dialogPasienLayout.setHorizontalGroup(
            dialogPasienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogPasienLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dialogPasienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                    .addGroup(dialogPasienLayout.createSequentialGroup()
                        .addComponent(btnDialogKembaliPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDialogMasukanPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        dialogPasienLayout.setVerticalGroup(
            dialogPasienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dialogPasienLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dialogPasienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDialogMasukanPasien, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(btnDialogKembaliPasien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        dialogDokter.setSize(new java.awt.Dimension(450, 300));

        tableDialogDokter.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(tableDialogDokter);

        btnDialogKembaliDokter.setText("Kembali");
        btnDialogKembaliDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDialogKembaliDokterActionPerformed(evt);
            }
        });

        btnDialogMasukanDokter.setText("Masukan");
        btnDialogMasukanDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDialogMasukanDokterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dialogDokterLayout = new javax.swing.GroupLayout(dialogDokter.getContentPane());
        dialogDokter.getContentPane().setLayout(dialogDokterLayout);
        dialogDokterLayout.setHorizontalGroup(
            dialogDokterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogDokterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dialogDokterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                    .addGroup(dialogDokterLayout.createSequentialGroup()
                        .addComponent(btnDialogKembaliDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDialogMasukanDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        dialogDokterLayout.setVerticalGroup(
            dialogDokterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dialogDokterLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dialogDokterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDialogMasukanDokter, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(btnDialogKembaliDokter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        dialogTindakan.setSize(new java.awt.Dimension(450, 300));

        tableDialogTindakan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(tableDialogTindakan);

        btnDialogKembaliTindakan.setText("Kembali");
        btnDialogKembaliTindakan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDialogKembaliTindakanActionPerformed(evt);
            }
        });

        btnDialogMasukanTindakan.setText("Masukan");
        btnDialogMasukanTindakan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDialogMasukanTindakanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dialogTindakanLayout = new javax.swing.GroupLayout(dialogTindakan.getContentPane());
        dialogTindakan.getContentPane().setLayout(dialogTindakanLayout);
        dialogTindakanLayout.setHorizontalGroup(
            dialogTindakanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogTindakanLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dialogTindakanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                    .addGroup(dialogTindakanLayout.createSequentialGroup()
                        .addComponent(btnDialogKembaliTindakan, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDialogMasukanTindakan, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        dialogTindakanLayout.setVerticalGroup(
            dialogTindakanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dialogTindakanLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dialogTindakanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDialogMasukanTindakan, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(btnDialogKembaliTindakan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        dialogLaporan.setTitle("Pilih Tanggal");
        dialogLaporan.setSize(new java.awt.Dimension(250, 260));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("PILIH TANGGAL AWAL DAN AKHIR");

        jLabel5.setText("Tanggal Awal");

        jLabel6.setText("Tanggal Akhir");

        jButton1.setText("Lihat Laporan");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dialogLaporanLayout = new javax.swing.GroupLayout(dialogLaporan.getContentPane());
        dialogLaporan.getContentPane().setLayout(dialogLaporanLayout);
        dialogLaporanLayout.setHorizontalGroup(
            dialogLaporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
            .addGroup(dialogLaporanLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(dialogLaporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dialogLaporanLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dialogLaporanLayout.createSequentialGroup()
                        .addGroup(dialogLaporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tgl_akhir, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tgl_awal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, dialogLaporanLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(20, 20, 20))))
        );
        dialogLaporanLayout.setVerticalGroup(
            dialogLaporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogLaporanLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tgl_awal, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tgl_akhir, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
                .addContainerGap())
        );

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Kunjungan");
        setPreferredSize(new java.awt.Dimension(1280, 820));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Form Kunjungan"));
        jPanel1.setToolTipText("");

        jLabel1.setText("Kode Kunjungan");

        jLabel2.setText("Tanggal Kunjungan");

        jLabel13.setText("Kode Dokter");

        jLabel14.setText("Nama Dokter");

        jLabel15.setText("Jenis Kelamin Dokter");

        btnCariPasien1.setText("Cari");
        btnCariPasien1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariPasien1ActionPerformed(evt);
            }
        });

        jLabel18.setText("Spesialis Dokter");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNmDokter)
                    .addComponent(txtJkDokter)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtKdDokter)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCariPasien1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel1)
                                .addComponent(txtTglKunjungan, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                                .addComponent(jLabel2)
                                .addComponent(txtKdKunjungan))
                            .addComponent(jLabel18))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txtSpesialisDokter))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(7, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtKdKunjungan, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTglKunjungan, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtKdDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCariPasien1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNmDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtJkDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSpesialisDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Daftar Kunjungan"));

        tableKunjungan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Kode Petugas", "Nama Petugas", "No Handphone Petugas"
            }
        ));
        tableKunjungan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableKunjunganMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableKunjungan);

        btnLaporan.setText("Cetak Laporan");
        btnLaporan.setPreferredSize(new java.awt.Dimension(97, 48));
        btnLaporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLaporanActionPerformed(evt);
            }
        });

        btnStruk.setText("Cetak Struk");
        btnStruk.setPreferredSize(new java.awt.Dimension(97, 48));
        btnStruk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStrukActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnStruk, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnStruk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1)
                .addGap(12, 12, 12))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Form Pasien"));
        jPanel3.setPreferredSize(new java.awt.Dimension(254, 404));

        jLabel9.setText("Keluhan Pasien");

        jLabel7.setText("Nama Pasien");

        jLabel4.setText("Kode Pasien");

        btnCariPasien.setText("Cari");
        btnCariPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariPasienActionPerformed(evt);
            }
        });

        jLabel8.setText("Jenis Kelamin Pasien");

        jLabel16.setText("Umur Pasien");

        jLabel17.setText("Nomor Handphone Pasien");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(142, 142, 142))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNmPasien)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel7)
                                    .addComponent(txtKdPasien)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel17))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCariPasien))
                            .addComponent(txtKeluhanPasien)
                            .addComponent(txtJkPasien)
                            .addComponent(txtUmurPasien)
                            .addComponent(txtHpPasien))
                        .addContainerGap())))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtKdPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCariPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNmPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtJkPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUmurPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtHpPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtKeluhanPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnKosong.setText("Kosongkan");
        btnKosong.setPreferredSize(new java.awt.Dimension(97, 48));
        btnKosong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKosongActionPerformed(evt);
            }
        });

        btnHapus.setText("Hapus");
        btnHapus.setPreferredSize(new java.awt.Dimension(97, 48));
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        btnUbah.setText("Ubah");
        btnUbah.setPreferredSize(new java.awt.Dimension(97, 48));
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });

        btnTambah.setText("Tambah");
        btnTambah.setPreferredSize(new java.awt.Dimension(97, 48));
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnTambah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUbah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnHapus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnKosong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTambah, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnUbah, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHapus, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnKosong, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        if(txtKdDokter.getText().equals("")|| txtTglKunjungan.getText().equals("") || txtKdPasien.getText().equals("") || txtKeluhanPasien.getText().equals("")){
            JOptionPane.showMessageDialog(rootPane, "Data tidak boleh ada yang kosong", "Pemberitahuan", JOptionPane.WARNING_MESSAGE);
        }else{
            ubah();
        }
        loadData();
        kosong();
    }//GEN-LAST:event_btnUbahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        try {
            hapus();
            loadData();
            kosong();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Tidak ada data yang dipilih");
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnKosongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKosongActionPerformed
        kosong();
    }//GEN-LAST:event_btnKosongActionPerformed

    private void tableKunjunganMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableKunjunganMouseClicked
        klik();
    }//GEN-LAST:event_tableKunjunganMouseClicked

    private void btnLaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLaporanActionPerformed
        dialogLaporan.setLocationRelativeTo(null);
        dialogLaporan.setVisible(true);
    }//GEN-LAST:event_btnLaporanActionPerformed

    private void btnDialogKembaliPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDialogKembaliPasienActionPerformed
        dialogPasien.setVisible(false);
    }//GEN-LAST:event_btnDialogKembaliPasienActionPerformed

    private void btnDialogMasukanPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDialogMasukanPasienActionPerformed
        String getKd = tableDialogPasien.getValueAt(tableDialogPasien.getSelectedRow(), 0).toString();
        String getNm = tableDialogPasien.getValueAt(tableDialogPasien.getSelectedRow(), 1).toString();
        String getUmur = tableDialogPasien.getValueAt(tableDialogPasien.getSelectedRow(), 2).toString();
        String getJk = tableDialogPasien.getValueAt(tableDialogPasien.getSelectedRow(), 3).toString();
        String getHp = tableDialogPasien.getValueAt(tableDialogPasien.getSelectedRow(), 4).toString();

        txtKdPasien.setText(getKd);
        txtNmPasien.setText(getNm);
        txtJkPasien.setText(getJk);
        txtUmurPasien.setText(getUmur);
        txtHpPasien.setText(getHp);
        dialogPasien.setVisible(false);
    }//GEN-LAST:event_btnDialogMasukanPasienActionPerformed

    private void btnDialogKembaliDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDialogKembaliDokterActionPerformed
        dialogDokter.setVisible(false);
    }//GEN-LAST:event_btnDialogKembaliDokterActionPerformed

    private void btnDialogMasukanDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDialogMasukanDokterActionPerformed
        String getKd = tableDialogDokter.getValueAt(tableDialogDokter.getSelectedRow(), 0).toString();
        String getNm = tableDialogDokter.getValueAt(tableDialogDokter.getSelectedRow(), 1).toString();
        String getJk = tableDialogDokter.getValueAt(tableDialogDokter.getSelectedRow(), 2).toString();
        String getSpesialis = tableDialogDokter.getValueAt(tableDialogDokter.getSelectedRow(), 3).toString();
  
        txtKdDokter.setText(getKd);
        txtNmDokter.setText(getNm);
        txtJkDokter.setText(getJk);
        txtSpesialisDokter.setText(getSpesialis);
        dialogDokter.setVisible(false);
    }//GEN-LAST:event_btnDialogMasukanDokterActionPerformed

    private void btnDialogKembaliTindakanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDialogKembaliTindakanActionPerformed
        dialogTindakan.setVisible(false);
    }//GEN-LAST:event_btnDialogKembaliTindakanActionPerformed

    private void btnDialogMasukanTindakanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDialogMasukanTindakanActionPerformed

    }//GEN-LAST:event_btnDialogMasukanTindakanActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        if(txtKdKunjungan.getText().equals("")|| txtTglKunjungan.getText().equals("") || txtKdDokter.getText().equals("") || txtKdPasien.getText().equals("")|| txtKeluhanPasien.getText().equals("")){
            JOptionPane.showMessageDialog(rootPane,"Data tidak boleh ada yang kosong","Pemberitahuan",JOptionPane.WARNING_MESSAGE);
        }else{
            simpan();
        }
        loadData();
        autoNumber();
        kosong();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnCariPasien1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariPasien1ActionPerformed
        dialogDokter.setLocationRelativeTo(null);
        daftarDokter();
        dialogDokter.setVisible(true);
    }//GEN-LAST:event_btnCariPasien1ActionPerformed

    private void btnCariPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariPasienActionPerformed
        dialogPasien.setLocationRelativeTo(null);
        daftarPasien();
        dialogPasien.setVisible(true);
    }//GEN-LAST:event_btnCariPasienActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            cetak();
        } catch (SQLException ex) {
            Logger.getLogger(kunjungan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnStrukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStrukActionPerformed
        try {
            cetakStruk();
        } catch (SQLException ex) {
            Logger.getLogger(kunjungan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnStrukActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCariPasien;
    private javax.swing.JButton btnCariPasien1;
    private javax.swing.JButton btnDialogKembaliDokter;
    private javax.swing.JButton btnDialogKembaliPasien;
    private javax.swing.JButton btnDialogKembaliTindakan;
    private javax.swing.JButton btnDialogMasukanDokter;
    private javax.swing.JButton btnDialogMasukanPasien;
    private javax.swing.JButton btnDialogMasukanTindakan;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKosong;
    private javax.swing.JButton btnLaporan;
    private javax.swing.JButton btnStruk;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnUbah;
    private javax.swing.JDialog dialogDokter;
    private javax.swing.JDialog dialogLaporan;
    private javax.swing.JDialog dialogPasien;
    private javax.swing.JDialog dialogTindakan;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable tableDialogDokter;
    private javax.swing.JTable tableDialogPasien;
    private javax.swing.JTable tableDialogTindakan;
    private javax.swing.JTable tableKunjungan;
    private com.toedter.calendar.JDateChooser tgl_akhir;
    private com.toedter.calendar.JDateChooser tgl_awal;
    private javax.swing.JTextField txtHpPasien;
    private javax.swing.JTextField txtJkDokter;
    private javax.swing.JTextField txtJkPasien;
    private javax.swing.JTextField txtKdDokter;
    private javax.swing.JTextField txtKdKunjungan;
    private javax.swing.JTextField txtKdPasien;
    private javax.swing.JTextField txtKeluhanPasien;
    private javax.swing.JTextField txtNmDokter;
    private javax.swing.JTextField txtNmPasien;
    private javax.swing.JTextField txtSpesialisDokter;
    private javax.swing.JTextField txtTglKunjungan;
    private javax.swing.JTextField txtUmurPasien;
    // End of variables declaration//GEN-END:variables

}
