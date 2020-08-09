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
public class rekam extends javax.swing.JInternalFrame {

    /**
     * Creates new form rekam
     */
    DefaultTableModel tabel = new DefaultTableModel();
    DefaultTableModel tabel2 = new DefaultTableModel();
    DefaultTableModel tabel3 = new DefaultTableModel();
    int biaya = 0;
    public rekam() {
        initComponents();
        autoNumber();
        loadData();
        loadDataTindakan(txtKdRekam.getText());
        loadDataObat(txtKdRekam.getText());
        disable();
        kosong();
    }
    
    public void tambahTotal(int harga) {
        biaya = biaya + harga;
        txtTotalBiaya.setText(Integer.toString(biaya));
    }
    
    public void kurangTotal(int harga) {
        biaya = biaya - harga;
        txtTotalBiaya.setText(Integer.toString(biaya));
    }
    
    public void disable(){
        txtKdRekam.setEnabled(false);
        txtTglRekam.setEnabled(false);
        txtKdKunjungan.setEnabled(false);
        txtKdPasien.setEnabled(false);
        txtNmPasien.setEnabled(false);
        txtKeluhanPasien.setEnabled(false);
        txtKdDokter.setEnabled(false);
        txtNmDokter.setEnabled(false);
        txtKdTindakan.setEnabled(false);
        txtNmTindakan.setEnabled(false);
        txtHargaTindakan.setEnabled(false);
        btnHapusTindakan.setEnabled(false);
        txtKdObat.setEnabled(false);
        txtNmObat.setEnabled(false);
        txtHargaObat.setEnabled(false);
        btnHapusObat.setEnabled(false);
        btnStruk.setEnabled(false);
    }
    
    public void kosong() {
        txtKdRekam.setText("");
        txtTglRekam.setText("");
        txtKdKunjungan.setText("");
        txtKdPasien.setText("");
        txtNmPasien.setText("");
        txtKeluhanPasien.setText("");
        txtKdDokter.setText("");
        txtNmDokter.setText("");
        txtDiagnosa.setText("");
        biaya = 0;
        txtTotalBiaya.setText(Integer.toString(biaya));
        btnTambah.setEnabled(true);
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
        btnStruk.setEnabled(false);
        autoNumber();
        loadDataTindakan(txtKdRekam.getText());
        loadDataObat(txtKdRekam.getText());
        tanggal();
    }
    
    private void tanggal(){
        Calendar kal = new GregorianCalendar();
        int tahun = kal.get(Calendar.YEAR);
        int bulan = kal.get(Calendar.MONTH);
        int tanggal = kal.get(Calendar.DAY_OF_MONTH);
        txtTglRekam.setText(tahun+"-"+(bulan+1)+"-"+tanggal);
    }
    
    private void autoNumber() {
        try {
            java.sql.Connection line_konek = (com.mysql.jdbc.Connection)klinik.koneksi.koneksi.getDB();
            java.sql.Statement line_statemen = line_konek.createStatement();
            String query_bukaTabel = "SELECT MAX(RIGHT(kd_rekam_medis,4)) AS nomor FROM t_rekam_medis";
            java.sql.ResultSet line_result = line_statemen.executeQuery(query_bukaTabel);
            if (line_result.first() == false) {
                txtKdRekam.setText("RMS0001");
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
                txtKdRekam.setText("RMS" + nomor);
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
            String querry = "SELECT kd_rekam_medis, tgl_rekam_medis, kd_kunjungan, hasil_diagnosa, total_biaya FROM t_rekam_medis";
            java.sql.ResultSet rs = stm.executeQuery(querry);
            tableRekamMedis.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(Exception e){
            System.out.println("Error Load Data : " + e);
        }
    }
    
    private void loadDataTindakan(String kd_rekam){
        tabel.getDataVector().removeAllElements();
        tabel.fireTableDataChanged();
        try{
            java.sql.Connection conn = (Connection)klinik.koneksi.koneksi.getDB();
            java.sql.Statement stm = conn.createStatement();
            String querry = "SELECT kd_tindakan, tindakan, harga FROM t_detail_tindakan_rekam_medis WHERE kd_rekam_medis ='"+ kd_rekam +"'";
            java.sql.ResultSet rs = stm.executeQuery(querry);
            tableTindakan.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(Exception e){
            System.out.println("Error Load Data Tindakan : " + e);
        }
    }
    
    private void loadDataObat(String kd_rekam){
        tabel.getDataVector().removeAllElements();
        tabel.fireTableDataChanged();
        try{
            java.sql.Connection conn = (Connection)klinik.koneksi.koneksi.getDB();
            java.sql.Statement stm = conn.createStatement();
            String querry = "SELECT kd_obat, obat, harga FROM t_detail_obat_rekam_medis WHERE kd_rekam_medis ='"+ kd_rekam +"'";
            java.sql.ResultSet rs = stm.executeQuery(querry);
            tableObat.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(Exception e){
            System.out.println("Error Load Data Obat : " + e);
        }
    }
    
    private void daftarKunjungan(){

       tabel2.getDataVector().removeAllElements();
        tabel2.fireTableDataChanged();
        try{
            java.sql.Connection conn = (Connection)klinik.koneksi.koneksi.getDB();
            java.sql.Statement stm = conn.createStatement();
            String querry_bukatable="SELECT kd_kunjungan, kd_pasien, keluhan, kd_dokter, status_rekam_medis from t_kunjungan";
            java.sql.ResultSet rs = stm.executeQuery(querry_bukatable);
            tableDialogKunjungan.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(Exception e){
            System.out.println("Errot load daftar kunjungan : " + e);
        }
    }
    
    private void daftarTindakan(){
       tabel2.getDataVector().removeAllElements();
        tabel2.fireTableDataChanged();
        try{
            java.sql.Connection conn = (Connection)klinik.koneksi.koneksi.getDB();
            java.sql.Statement stm = conn.createStatement();
            String querry_bukatable="SELECT kd_tindakan, tindakan, harga_tindakan from t_tindakan";
            java.sql.ResultSet rs = stm.executeQuery(querry_bukatable);
            tableDialogTindakan.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(Exception e){
            System.out.println("Errot load daftar tindakan : " + e);
        }
    }
    
    private void daftarObat(){
       tabel2.getDataVector().removeAllElements();
        tabel2.fireTableDataChanged();
        try{
            java.sql.Connection conn = (Connection)klinik.koneksi.koneksi.getDB();
            java.sql.Statement stm = conn.createStatement();
            String querry_bukatable="SELECT kd_obat, nama_obat, harga_obat from t_obat";
            java.sql.ResultSet rs = stm.executeQuery(querry_bukatable);
            tableDialogObat.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(Exception e){
            System.out.println("Errot load daftar obat : " + e);
        }
    }
        
    private void simpan(){
        String kd_rekam = txtKdRekam.getText();
        String tgl_rekam = txtTglRekam.getText();
        String kd_kunjungan = txtKdKunjungan.getText();
        String hasil_diagnosa = txtDiagnosa.getText();
        String total_biaya = txtTotalBiaya.getText();
        
       try{
           Connection conn =(Connection)klinik.koneksi.koneksi.getDB();
           java.sql.Statement stm = conn.createStatement();
           String querry ="INSERT INTO t_rekam_medis VALUES ('"+kd_rekam+"','"+tgl_rekam+"', '"+kd_kunjungan+"', '"+hasil_diagnosa+"', '"+total_biaya+"')";
           stm.executeUpdate(querry);
           JOptionPane.showMessageDialog(this, "Berhasil menambahkan rekam medis pasien baru", "Sukses", JOptionPane.INFORMATION_MESSAGE);
       }catch(Exception e){
           JOptionPane.showMessageDialog(this, "Gagal Memasukkan Data !", "Peringatan", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
       }
    }
    
    private void simpanTindakan(){
        String kd_rekam_medis = txtKdRekam.getText();
        String kd_tindakan = txtKdTindakan.getText();
        String nm_tindakan = txtNmTindakan.getText();
        String harga = txtHargaTindakan.getText();
       try{
           Connection conn =(Connection)klinik.koneksi.koneksi.getDB();
           java.sql.Statement stm = conn.createStatement();
           String querry ="INSERT INTO t_detail_tindakan_rekam_medis (kd_rekam_medis, kd_tindakan, tindakan, harga) VALUES ('"+kd_rekam_medis+"','"+kd_tindakan+"', '"+nm_tindakan+"', '"+harga+"')";
           stm.executeUpdate(querry);
       }catch(Exception e){
           JOptionPane.showMessageDialog(this, "Gagal Memasukkan Data !", "Peringatan", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
       }
    }
    
    private void simpanObat(){
        String kd_rekam_medis = txtKdRekam.getText();
        String kd_obat = txtKdObat.getText();
        String nm_obat = txtNmObat.getText();
        String harga = txtHargaObat.getText();
       try{
           Connection conn =(Connection)klinik.koneksi.koneksi.getDB();
           java.sql.Statement stm = conn.createStatement();
           String querry ="INSERT INTO t_detail_obat_rekam_medis (kd_rekam_medis, kd_obat, obat, harga) VALUES ('"+kd_rekam_medis+"','"+kd_obat+"', '"+nm_obat+"', '"+harga+"')";
           stm.executeUpdate(querry);
       }catch(Exception e){
           JOptionPane.showMessageDialog(this, "Gagal Memasukkan Data !", "Peringatan", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
       }
    }
    
    private void ubah(){
        try{
            String kd_rekam = txtKdRekam.getText();
            String tgl_rekam = txtTglRekam.getText();
            String kd_kunjungan = txtKdKunjungan.getText();
            String hasil_diagnosa = txtDiagnosa.getText();
            String total_biaya = txtTotalBiaya.getText();
            
            Connection conn =(Connection)klinik.koneksi.koneksi.getDB();
            String querry ="UPDATE t_rekam_medis SET kd_kunjungan='"+kd_kunjungan+"', hasil_diagnosa='"+hasil_diagnosa+"', total_biaya='"+total_biaya+"' WHERE kd_rekam_medis='"+kd_rekam+"'";
            com.mysql.jdbc.PreparedStatement prepare = (PreparedStatement)conn.prepareStatement(querry);
            prepare.execute();
            JOptionPane.showMessageDialog(this, "Berhasil mengubah data rekam medis pasien", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            btnTambah.setEnabled(true);
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Gagal Mengubah Data !", "Peringatan", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
        }
    }
    
    private void updateStatus(){
        try{
            String kd_kunjungan = txtKdKunjungan.getText();
            String status = "Sudah Rekam Medis";
            
            Connection conn =(Connection)klinik.koneksi.koneksi.getDB();
            String querry ="UPDATE t_kunjungan SET status_rekam_medis='"+status+"' WHERE kd_kunjungan='"+kd_kunjungan+"'";
            com.mysql.jdbc.PreparedStatement prepare = (PreparedStatement)conn.prepareStatement(querry);
            prepare.execute();
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Gagal mengupdate status data kunjungan pasien !", "Peringatan", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
        }
    }
    
    private void rollbackStatus(){
        try{
            String kd_kunjungan = txtKdKunjungan.getText();
            String status = "Belum Rekam Medis";
            
            Connection conn =(Connection)klinik.koneksi.koneksi.getDB();
            String querry ="UPDATE t_kunjungan SET status_rekam_medis='"+status+"' WHERE kd_kunjungan='"+kd_kunjungan+"'";
            com.mysql.jdbc.PreparedStatement prepare = (PreparedStatement)conn.prepareStatement(querry);
            prepare.execute();
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Gagal mengupdate status data kunjungan pasien !", "Peringatan", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
        }
    }
    
    private void hapus(){
        int s_row = tableRekamMedis.getSelectedRow();
        if(s_row==-1){
            return;
        }
        String dataDelete = (String) tableRekamMedis.getValueAt(s_row,0);
        try{
            Connection conn = (Connection)klinik.koneksi.koneksi.getDB();
            String querry = "DELETE FROM t_rekam_medis WHERE kd_rekam_medis=?";
            java.sql.PreparedStatement stm = (PreparedStatement)conn.prepareStatement(querry);
            stm.setString(1,dataDelete);
            stm.executeUpdate();
            stm.close();
            
            String querry2 = "DELETE FROM t_detail_tindakan_rekam_medis WHERE kd_rekam_medis=?";
            java.sql.PreparedStatement stm2 = (PreparedStatement)conn.prepareStatement(querry2);
            stm2.setString(1,dataDelete);
            stm2.executeUpdate();
            stm2.close();
            
            String querry3 = "DELETE FROM t_detail_obat_rekam_medis WHERE kd_rekam_medis=?";
            java.sql.PreparedStatement stm3 = (PreparedStatement)conn.prepareStatement(querry3);
            stm3.setString(1,dataDelete);
            stm3.executeUpdate();
            stm3.close();
            
            JOptionPane.showMessageDialog(this, "Berhasil menghapus data rekam medis pasien", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            btnTambah.setEnabled(true);
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Tidak bisa menghapus data", "Hapus Data", JOptionPane.WARNING_MESSAGE);
        } finally {
            loadData();
        }
    }
    
    private void hapusTindakan(){
        int s_row = tableTindakan.getSelectedRow();
        if(s_row==-1){
            return;
        }
        String kd_rekam_medis = txtKdRekam.getText();
        String dataDelete = (String) tableTindakan.getValueAt(s_row,0);
        try{
            Connection conn = (Connection)klinik.koneksi.koneksi.getDB();
            String querry = "DELETE FROM t_detail_tindakan_rekam_medis WHERE kd_rekam_medis=? AND kd_tindakan=?";
            java.sql.PreparedStatement stm = (PreparedStatement)conn.prepareStatement(querry);
            stm.setString(1,kd_rekam_medis);
            stm.setString(2,dataDelete);
            stm.executeUpdate();
            stm.close();
            btnTambahTindakan.setEnabled(true);
        }catch(Exception e){
            System.out.println("Hapus Tindakan Gagal =" +e);
            JOptionPane.showMessageDialog(this, "Tidak bisa menghapus data", "Hapus Data", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void hapusObat(){
        int s_row = tableObat.getSelectedRow();
        if(s_row==-1){
            return;
        }
        String kd_rekam_medis = txtKdRekam.getText();
        String dataDelete = (String) tableObat.getValueAt(s_row,0);
        try{
            Connection conn = (Connection)klinik.koneksi.koneksi.getDB();
            String querry = "DELETE FROM t_detail_obat_rekam_medis WHERE kd_rekam_medis=? AND kd_obat=?";
            java.sql.PreparedStatement stm = (PreparedStatement)conn.prepareStatement(querry);
            stm.setString(1,kd_rekam_medis);
            stm.setString(2,dataDelete);
            stm.executeUpdate();
            stm.close();
            
            btnTambahObat.setEnabled(true);
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
        String getKdRekamMedis = tableRekamMedis.getValueAt(tableRekamMedis.getSelectedRow(), 0).toString();
        String getTglRekamMedis = tableRekamMedis.getValueAt(tableRekamMedis.getSelectedRow(), 1).toString();
        String getKdKunjungan = tableRekamMedis.getValueAt(tableRekamMedis.getSelectedRow(), 2).toString();
        String getDiagnosa = tableRekamMedis.getValueAt(tableRekamMedis.getSelectedRow(), 3).toString();
        String getTotalBiaya = tableRekamMedis.getValueAt(tableRekamMedis.getSelectedRow(), 4).toString();
        
        biaya = Integer.parseInt(getTotalBiaya);
        String sqlKunjungan = "SELECT * from t_kunjungan WHERE kd_kunjungan ='"+ getKdKunjungan +"'";
        try{
            Connection conn = (Connection)klinik.koneksi.koneksi.getDB();
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sqlKunjungan);
                if(hasil.next()){
                    String kd_pasien = hasil.getString("kd_pasien"); 
                    String kd_dokter = hasil.getString("kd_dokter"); 
                    String keluhan = hasil.getString("keluhan"); 
                    txtKdPasien.setText(kd_pasien);
                    txtKdDokter.setText(kd_dokter);
                    txtKeluhanPasien.setText(keluhan);
                    
                    String sqlDokter = "SELECT * from t_dokter WHERE kd_dokter ='"+ kd_dokter +"'";
                    try{
                        Connection conn2 = (Connection)klinik.koneksi.koneksi.getDB();
                        java.sql.Statement stat2 = conn2.createStatement();
                        ResultSet hasil2 = stat2.executeQuery(sqlDokter);
                            if(hasil2.next()){
                                String namaDokter = hasil2.getString("nama_dokter");             
                                txtNmDokter.setText(namaDokter);
                            } else {
                                System.out.println("Error get dokter");
                            }
                        }catch(SQLException e) {
                            System.out.println(e);
                    }  
                    
                    String sqlPasien = "SELECT * from t_pasien WHERE kd_pasien ='"+ kd_pasien +"'";
                    try{
                        Connection conn2 = (Connection)klinik.koneksi.koneksi.getDB();
                        java.sql.Statement stat2 = conn2.createStatement();
                        ResultSet hasil3 = stat2.executeQuery(sqlPasien);
                            if(hasil3.next()){
                                String namaDokter = hasil3.getString("nama_pasien");             
                                txtNmPasien.setText(namaDokter);
                            } else {
                                System.out.println("Error get pasien");
                            }
                        }catch(SQLException e) {
                            System.out.println(e);
                    }  
                } else {
                    System.out.println("Error get kunjungan");
                }
            }catch(SQLException e) {
                System.out.println(e);
        } 
        
        txtKdRekam.setText(getKdRekamMedis);
        txtKdKunjungan.setText(getKdKunjungan);
        txtDiagnosa.setText(getDiagnosa);
        txtTotalBiaya.setText(getTotalBiaya);
        loadDataTindakan(txtKdRekam.getText());
        loadDataObat(txtKdRekam.getText());
    }
    
    private void cetakStruk() throws SQLException {
        try {
            String KdRekam = txtKdRekam.getText();
            String KdKunjungan = txtKdKunjungan.getText();
            String KdPasien = txtKdPasien.getText();
            String KdDokter = txtKdDokter.getText();
            System.out.println(KdKunjungan + KdPasien + KdDokter);
            String namafile = "src/klinik/laporan/struk_rekam_medis.jasper"
                    + "";
            HashMap hash = new HashMap();
            hash.put("kd_rekam", KdRekam);
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
    
    private void klikTindakan() {
        btnTambahTindakan.setEnabled(false);
        btnHapusTindakan.setEnabled(true);
        String getKode = tableTindakan.getValueAt(tableTindakan.getSelectedRow(), 0).toString();
        String getNama = tableTindakan.getValueAt(tableTindakan.getSelectedRow(), 1).toString();
        String getHarga = tableTindakan.getValueAt(tableTindakan.getSelectedRow(), 2).toString();

        txtKdTindakan.setText(getKode);
        txtNmTindakan.setText(getNama);
        txtHargaTindakan.setText(getHarga);
    }
    
    private void klikObat() {
        btnTambahObat.setEnabled(false);
        btnHapusObat.setEnabled(true);
        String getKode = tableObat.getValueAt(tableObat.getSelectedRow(), 0).toString();
        String getNama = tableObat.getValueAt(tableObat.getSelectedRow(), 1).toString();
        String getHarga = tableObat.getValueAt(tableObat.getSelectedRow(), 2).toString();

        txtKdObat.setText(getKode);
        txtNmObat.setText(getNama);
        txtHargaObat.setText(getHarga);
    }
    
    private void cetak() throws SQLException {
        try {
            Date awal = tgl_awal.getDate();
            Date akhir = tgl_akhir.getDate();
            String namafile = "src/klinik/laporan/laporan_rekam_medis_pasien.jasper"
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dialogKunjungan = new javax.swing.JDialog();
        jScrollPane6 = new javax.swing.JScrollPane();
        tableDialogKunjungan = new javax.swing.JTable();
        btnDialogKembaliKunjungan = new javax.swing.JButton();
        btnDialogMasukanKunjungan = new javax.swing.JButton();
        dialogTindakan = new javax.swing.JDialog();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableDialogTindakan = new javax.swing.JTable();
        btnDialogKembaliTindakan = new javax.swing.JButton();
        btnDialogMasukanTindakan = new javax.swing.JButton();
        dialogObat = new javax.swing.JDialog();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableDialogObat = new javax.swing.JTable();
        btnDialogKembaliObat = new javax.swing.JButton();
        btnDialogMasukanObat = new javax.swing.JButton();
        dialogLaporan = new javax.swing.JDialog();
        jLabel3 = new javax.swing.JLabel();
        tgl_awal = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        tgl_akhir = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtKdRekam = new javax.swing.JTextField();
        txtTglRekam = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtKdKunjungan = new javax.swing.JTextField();
        txtKdPasien = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtKeluhanPasien = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        btnCariKunjungan = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        txtKdDokter = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtDiagnosa = new javax.swing.JTextField();
        txtNmPasien = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtNmDokter = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        txtKdTindakan = new javax.swing.JTextField();
        btnCariTindakan = new javax.swing.JButton();
        txtNmTindakan = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtHargaTindakan = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableTindakan = new javax.swing.JTable();
        btnTambahTindakan = new javax.swing.JButton();
        btnHapusTindakan = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableRekamMedis = new javax.swing.JTable();
        btnLaporan = new javax.swing.JButton();
        btnStruk = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        txtKdObat = new javax.swing.JTextField();
        btnCariObat = new javax.swing.JButton();
        txtNmObat = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        txtHargaObat = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableObat = new javax.swing.JTable();
        btnTambahObat = new javax.swing.JButton();
        btnHapusObat = new javax.swing.JButton();
        btnTambah = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnKosong = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        txtTotalBiaya = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        dialogKunjungan.setSize(new java.awt.Dimension(450, 300));

        tableDialogKunjungan.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane6.setViewportView(tableDialogKunjungan);

        btnDialogKembaliKunjungan.setText("Kembali");
        btnDialogKembaliKunjungan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDialogKembaliKunjunganActionPerformed(evt);
            }
        });

        btnDialogMasukanKunjungan.setText("Masukan");
        btnDialogMasukanKunjungan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDialogMasukanKunjunganActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dialogKunjunganLayout = new javax.swing.GroupLayout(dialogKunjungan.getContentPane());
        dialogKunjungan.getContentPane().setLayout(dialogKunjunganLayout);
        dialogKunjunganLayout.setHorizontalGroup(
            dialogKunjunganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogKunjunganLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dialogKunjunganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                    .addGroup(dialogKunjunganLayout.createSequentialGroup()
                        .addComponent(btnDialogKembaliKunjungan, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDialogMasukanKunjungan, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        dialogKunjunganLayout.setVerticalGroup(
            dialogKunjunganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dialogKunjunganLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dialogKunjunganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDialogMasukanKunjungan, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(btnDialogKembaliKunjungan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        dialogObat.setSize(new java.awt.Dimension(450, 300));

        tableDialogObat.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane5.setViewportView(tableDialogObat);

        btnDialogKembaliObat.setText("Kembali");
        btnDialogKembaliObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDialogKembaliObatActionPerformed(evt);
            }
        });

        btnDialogMasukanObat.setText("Masukan");
        btnDialogMasukanObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDialogMasukanObatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dialogObatLayout = new javax.swing.GroupLayout(dialogObat.getContentPane());
        dialogObat.getContentPane().setLayout(dialogObatLayout);
        dialogObatLayout.setHorizontalGroup(
            dialogObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogObatLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dialogObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                    .addGroup(dialogObatLayout.createSequentialGroup()
                        .addComponent(btnDialogKembaliObat, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDialogMasukanObat, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        dialogObatLayout.setVerticalGroup(
            dialogObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dialogObatLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dialogObatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDialogMasukanObat, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(btnDialogKembaliObat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        dialogLaporan.setTitle("Pilih Tanggal");
        dialogLaporan.setSize(new java.awt.Dimension(250, 260));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("PILIH TANGGAL AWAL DAN AKHIR");

        jLabel6.setText("Tanggal Awal");

        jLabel7.setText("Tanggal Akhir");

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
                        .addComponent(jLabel7)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dialogLaporanLayout.createSequentialGroup()
                        .addGroup(dialogLaporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tgl_akhir, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tgl_awal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, dialogLaporanLayout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(20, 20, 20))))
        );
        dialogLaporanLayout.setVerticalGroup(
            dialogLaporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogLaporanLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tgl_awal, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
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
        setTitle("Rekam Medis Pasien");
        setSize(new java.awt.Dimension(1000, 820));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Form Rekam Medis"));
        jPanel1.setToolTipText("");

        jLabel1.setText("Kode Rekam Medis");

        jLabel2.setText("Tanggal Rekam Medis");

        jLabel13.setText("Kode Kunjungan");

        jLabel14.setText("Kode Pasien");

        jLabel15.setText("Keluhan Pasien");

        btnCariKunjungan.setText("Cari");
        btnCariKunjungan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariKunjunganActionPerformed(evt);
            }
        });

        jLabel18.setText("Kode Dokter");

        jLabel19.setText("Hasil DIagnosa");

        jLabel16.setText("Nama Pasien");

        jLabel20.setText("Nama Dokter");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtKdKunjungan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCariKunjungan))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(txtKdPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(txtNmPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(txtKeluhanPasien)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(txtKdDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(txtNmDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(txtDiagnosa)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(txtKdRekam, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(txtTglRekam, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel13)
                            .addComponent(jLabel15)
                            .addComponent(jLabel19))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtKdRekam, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTglRekam, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(7, 7, 7)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtKdKunjungan, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCariKunjungan, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtKdPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNmPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtKeluhanPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtKdDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNmDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDiagnosa, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Tindakan"));

        jLabel17.setText("Kode Tindakan");

        btnCariTindakan.setText("Cari");
        btnCariTindakan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariTindakanActionPerformed(evt);
            }
        });

        jLabel21.setText("Tindakan");

        jLabel22.setText("Harga (Rp.)");

        tableTindakan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "No", "Kode Tindakan", "Tindakan", "Harga"
            }
        ));
        tableTindakan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableTindakanMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tableTindakan);

        btnTambahTindakan.setText("Tambah");
        btnTambahTindakan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahTindakanActionPerformed(evt);
            }
        });

        btnHapusTindakan.setText("Hapus");
        btnHapusTindakan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusTindakanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnTambahTindakan, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHapusTindakan, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(jLabel21)
                            .addComponent(jLabel22))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txtHargaTindakan)
                    .addComponent(txtNmTindakan)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtKdTindakan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCariTindakan)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtKdTindakan, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCariTindakan, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNmTindakan, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtHargaTindakan, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTambahTindakan, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHapusTindakan, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Daftar Rekam Medis"));

        tableRekamMedis.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Kode Rekam Medis", "Tanggal Rekam Medis", "Kode Kunjungan", "Hasil Diagnosa", "Total Biaya"
            }
        ));
        tableRekamMedis.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableRekamMedisMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableRekamMedis);

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
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnStruk, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Obat"));

        jLabel23.setText("Kode Obat");

        btnCariObat.setText("Cari");
        btnCariObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariObatActionPerformed(evt);
            }
        });

        jLabel24.setText("Nama Obat");

        jLabel25.setText("Harga (Rp.)");

        tableObat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "No", "Kode Obat", "Nama Obat", "Harga"
            }
        ));
        tableObat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableObatMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tableObat);

        btnTambahObat.setText("Tambah");
        btnTambahObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahObatActionPerformed(evt);
            }
        });

        btnHapusObat.setText("Hapus");
        btnHapusObat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusObatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnTambahObat, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHapusObat, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23)
                            .addComponent(jLabel24)
                            .addComponent(jLabel25))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txtHargaObat)
                    .addComponent(txtNmObat)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txtKdObat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCariObat)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtKdObat, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCariObat, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNmObat, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtHargaObat, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTambahObat, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHapusObat, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        btnTambah.setText("Tambah");
        btnTambah.setPreferredSize(new java.awt.Dimension(97, 48));
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnUbah.setText("Ubah");
        btnUbah.setPreferredSize(new java.awt.Dimension(97, 48));
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });

        btnHapus.setText("Hapus");
        btnHapus.setPreferredSize(new java.awt.Dimension(97, 48));
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        btnKosong.setText("Kosongkan");
        btnKosong.setPreferredSize(new java.awt.Dimension(97, 48));
        btnKosong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKosongActionPerformed(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.setSize(new java.awt.Dimension(100, 66));

        txtTotalBiaya.setFont(new java.awt.Font("Lato", 3, 25)); // NOI18N
        txtTotalBiaya.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txtTotalBiaya.setText("Rp. 432.000");

        jLabel5.setFont(new java.awt.Font("Lato", 2, 18)); // NOI18N
        jLabel5.setText("Total biaya yang harus dibayar  (Rp.) :");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtTotalBiaya)
                .addGap(25, 25, 25))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(2, 2, 2))
                    .addComponent(txtTotalBiaya, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTambah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUbah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnHapus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnKosong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnTambah, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnUbah, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHapus, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnKosong, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCariKunjunganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariKunjunganActionPerformed
        dialogKunjungan.setLocationRelativeTo(null);
        daftarKunjungan();
        dialogKunjungan.setVisible(true);
    }//GEN-LAST:event_btnCariKunjunganActionPerformed

    private void btnCariTindakanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariTindakanActionPerformed
        dialogTindakan.setLocationRelativeTo(null);
        daftarTindakan();
        dialogTindakan.setVisible(true);
    }//GEN-LAST:event_btnCariTindakanActionPerformed

    private void btnTambahTindakanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahTindakanActionPerformed
        if(txtKdTindakan.getText().equals("")|| txtNmTindakan.getText().equals("") || txtHargaTindakan.getText().equals("")){
            JOptionPane.showMessageDialog(rootPane,"Data tidak boleh ada yang kosong","Pemberitahuan",JOptionPane.WARNING_MESSAGE);
        }else{
            simpanTindakan();
            String harga = txtHargaTindakan.getText();
            tambahTotal(Integer.parseInt(harga));
            txtKdTindakan.setText("");
            txtNmTindakan.setText("");
            txtHargaTindakan.setText("");
        }
        loadDataTindakan(txtKdRekam.getText());
    }//GEN-LAST:event_btnTambahTindakanActionPerformed

    private void btnHapusTindakanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusTindakanActionPerformed
        try {
            hapusTindakan();
            String harga = txtHargaTindakan.getText();
            kurangTotal(Integer.parseInt(harga));
            txtKdTindakan.setText("");
            txtNmTindakan.setText("");
            txtHargaTindakan.setText("");
            loadDataTindakan(txtKdRekam.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Tidak ada data yang dipilih");
        }
    }//GEN-LAST:event_btnHapusTindakanActionPerformed

    private void tableRekamMedisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableRekamMedisMouseClicked
        klik();
    }//GEN-LAST:event_tableRekamMedisMouseClicked

    private void btnLaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLaporanActionPerformed
        dialogLaporan.setLocationRelativeTo(null);
        dialogLaporan.setVisible(true);
    }//GEN-LAST:event_btnLaporanActionPerformed

    private void btnCariObatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariObatActionPerformed
        dialogObat.setLocationRelativeTo(null);
        daftarObat();
        dialogObat.setVisible(true);
    }//GEN-LAST:event_btnCariObatActionPerformed

    private void btnTambahObatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahObatActionPerformed
        if(txtKdObat.getText().equals("")|| txtNmObat.getText().equals("") || txtHargaObat.getText().equals("")){
            JOptionPane.showMessageDialog(rootPane,"Data tidak boleh ada yang kosong","Pemberitahuan",JOptionPane.WARNING_MESSAGE);
        }else{
            simpanObat();
            String harga = txtHargaObat.getText();
            tambahTotal(Integer.parseInt(harga));
            txtKdObat.setText("");
            txtNmObat.setText("");
            txtHargaObat.setText("");
            loadDataObat(txtKdRekam.getText());
        }
        
    }//GEN-LAST:event_btnTambahObatActionPerformed

    private void btnHapusObatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusObatActionPerformed
        try {
            hapusObat();
            String harga = txtHargaObat.getText();
            kurangTotal(Integer.parseInt(harga));
            txtKdObat.setText("");
            txtNmObat.setText("");
            txtHargaObat.setText("");
            loadDataObat(txtKdRekam.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Tidak ada data yang dipilih");
        }
    }//GEN-LAST:event_btnHapusObatActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        if(txtKdRekam.getText().equals("")|| txtTglRekam.getText().equals("") || txtKdKunjungan.getText().equals("") || txtDiagnosa.getText().equals("")){
            JOptionPane.showMessageDialog(rootPane,"Data tidak boleh ada yang kosong","Pemberitahuan",JOptionPane.WARNING_MESSAGE);
        }else{
            if(Integer.parseInt(txtTotalBiaya.getText()) > 0){
                simpan();
                updateStatus();
                loadData();
                autoNumber();
                loadDataTindakan(txtKdRekam.getText());
                loadDataObat(txtKdRekam.getText());
                biaya = 0;
                kosong();
            } else {
                JOptionPane.showMessageDialog(rootPane,"Tidak dapat menambah rekam medis dengan total biaya Rp. 0","Pemberitahuan",JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        if(txtKdRekam.getText().equals("")|| txtTglRekam.getText().equals("") || txtKdKunjungan.getText().equals("") || txtDiagnosa.getText().equals("")){
            JOptionPane.showMessageDialog(rootPane, "Data tidak boleh ada yang kosong", "Pemberitahuan", JOptionPane.WARNING_MESSAGE);
        }else{
            ubah();
            biaya = 0;
            loadDataTindakan(txtKdRekam.getText());
            loadDataObat(txtKdRekam.getText());
            loadData();
            kosong();
        } 
    }//GEN-LAST:event_btnUbahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        try {
            hapus();
            rollbackStatus();
            loadData();
            kosong();
            loadDataTindakan(txtKdRekam.getText());
            loadDataObat(txtKdRekam.getText());
            biaya = 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Tidak ada data yang dipilih");
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnKosongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKosongActionPerformed
        kosong();
    }//GEN-LAST:event_btnKosongActionPerformed

    private void btnStrukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStrukActionPerformed
        try {
            cetakStruk();
        } catch (SQLException ex) {
            Logger.getLogger(rekam.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnStrukActionPerformed

    private void btnDialogKembaliTindakanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDialogKembaliTindakanActionPerformed
        dialogTindakan.setVisible(false);
    }//GEN-LAST:event_btnDialogKembaliTindakanActionPerformed

    private void btnDialogMasukanTindakanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDialogMasukanTindakanActionPerformed
        String getKd = tableDialogTindakan.getValueAt(tableDialogTindakan.getSelectedRow(), 0).toString();
        String getNm = tableDialogTindakan.getValueAt(tableDialogTindakan.getSelectedRow(), 1).toString();
        String getHarga = tableDialogTindakan.getValueAt(tableDialogTindakan.getSelectedRow(), 2).toString();
  
        txtKdTindakan.setText(getKd);
        txtNmTindakan.setText(getNm);
        txtHargaTindakan.setText(getHarga);
        dialogTindakan.setVisible(false);
    }//GEN-LAST:event_btnDialogMasukanTindakanActionPerformed

    private void btnDialogKembaliObatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDialogKembaliObatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDialogKembaliObatActionPerformed

    private void btnDialogMasukanObatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDialogMasukanObatActionPerformed
        String getKd = tableDialogObat.getValueAt(tableDialogObat.getSelectedRow(), 0).toString();
        String getNm = tableDialogObat.getValueAt(tableDialogObat.getSelectedRow(), 1).toString();
        String getHarga = tableDialogObat.getValueAt(tableDialogObat.getSelectedRow(), 2).toString();
  
        txtKdObat.setText(getKd);
        txtNmObat.setText(getNm);
        txtHargaObat.setText(getHarga);
        dialogObat.setVisible(false);
    }//GEN-LAST:event_btnDialogMasukanObatActionPerformed

    private void btnDialogKembaliKunjunganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDialogKembaliKunjunganActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDialogKembaliKunjunganActionPerformed

    private void btnDialogMasukanKunjunganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDialogMasukanKunjunganActionPerformed
        String getKdKunjungan = tableDialogKunjungan.getValueAt(tableDialogKunjungan.getSelectedRow(), 0).toString();
        String getKdPasien = tableDialogKunjungan.getValueAt(tableDialogKunjungan.getSelectedRow(), 1).toString();
        String getKeluhanPasien = tableDialogKunjungan.getValueAt(tableDialogKunjungan.getSelectedRow(), 2).toString();
        String getKdDokter = tableDialogKunjungan.getValueAt(tableDialogKunjungan.getSelectedRow(), 3).toString();
        String status = tableDialogKunjungan.getValueAt(tableDialogKunjungan.getSelectedRow(), 4).toString();
        
        if(status.equals("Sudah Rekam Medis")) {
            JOptionPane.showMessageDialog(rootPane, "Tidak dapat memasukan data kunjungan pasien yang sudah tercatat di rekam medis", "Pemberitahuan", JOptionPane.WARNING_MESSAGE);
        } else {
            String sqlPasien = "SELECT * from t_pasien WHERE kd_pasien ='"+ getKdPasien +"'";
            try{
                Connection conn = (Connection)klinik.koneksi.koneksi.getDB();
                java.sql.Statement stat = conn.createStatement();
                ResultSet hasilPasien = stat.executeQuery(sqlPasien);
                    if(hasilPasien.next()){
                        String nama = hasilPasien.getString("nama_pasien");            
                        txtNmPasien.setText(nama);
                    } else {
                        System.out.println("Error get kunjungan");
                    }
                }catch(SQLException e) {
                    System.out.println(e);
            }

            String sqlDokter = "SELECT * from t_dokter WHERE kd_dokter ='"+ getKdDokter +"'";
            try{
                Connection conn = (Connection)klinik.koneksi.koneksi.getDB();
                java.sql.Statement stat = conn.createStatement();
                ResultSet hasil = stat.executeQuery(sqlDokter);
                    if(hasil.next()){
                        String nama = hasil.getString("nama_dokter");            
                        txtNmDokter.setText(nama);
                    } else {
                        System.out.println("Error get dokter");
                    }
                }catch(SQLException e) {
                    System.out.println(e);
            } 

            txtKdKunjungan.setText(getKdKunjungan);
            txtKdPasien.setText(getKdPasien);
            txtKeluhanPasien.setText(getKeluhanPasien);
            txtKdDokter.setText(getKdDokter);
            dialogKunjungan.setVisible(false);
        }
    }//GEN-LAST:event_btnDialogMasukanKunjunganActionPerformed

    private void tableTindakanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableTindakanMouseClicked
        klikTindakan();
    }//GEN-LAST:event_tableTindakanMouseClicked

    private void tableObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableObatMouseClicked
        klikObat();
    }//GEN-LAST:event_tableObatMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            cetak();
        } catch (SQLException ex) {
            Logger.getLogger(kunjungan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCariKunjungan;
    private javax.swing.JButton btnCariObat;
    private javax.swing.JButton btnCariTindakan;
    private javax.swing.JButton btnDialogKembaliKunjungan;
    private javax.swing.JButton btnDialogKembaliObat;
    private javax.swing.JButton btnDialogKembaliTindakan;
    private javax.swing.JButton btnDialogMasukanKunjungan;
    private javax.swing.JButton btnDialogMasukanObat;
    private javax.swing.JButton btnDialogMasukanTindakan;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnHapusObat;
    private javax.swing.JButton btnHapusTindakan;
    private javax.swing.JButton btnKosong;
    private javax.swing.JButton btnLaporan;
    private javax.swing.JButton btnStruk;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnTambahObat;
    private javax.swing.JButton btnTambahTindakan;
    private javax.swing.JButton btnUbah;
    private javax.swing.JDialog dialogKunjungan;
    private javax.swing.JDialog dialogLaporan;
    private javax.swing.JDialog dialogObat;
    private javax.swing.JDialog dialogTindakan;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable tableDialogKunjungan;
    private javax.swing.JTable tableDialogObat;
    private javax.swing.JTable tableDialogTindakan;
    private javax.swing.JTable tableObat;
    private javax.swing.JTable tableRekamMedis;
    private javax.swing.JTable tableTindakan;
    private com.toedter.calendar.JDateChooser tgl_akhir;
    private com.toedter.calendar.JDateChooser tgl_awal;
    private javax.swing.JTextField txtDiagnosa;
    private javax.swing.JTextField txtHargaObat;
    private javax.swing.JTextField txtHargaTindakan;
    private javax.swing.JTextField txtKdDokter;
    private javax.swing.JTextField txtKdKunjungan;
    private javax.swing.JTextField txtKdObat;
    private javax.swing.JTextField txtKdPasien;
    private javax.swing.JTextField txtKdRekam;
    private javax.swing.JTextField txtKdTindakan;
    private javax.swing.JTextField txtKeluhanPasien;
    private javax.swing.JTextField txtNmDokter;
    private javax.swing.JTextField txtNmObat;
    private javax.swing.JTextField txtNmPasien;
    private javax.swing.JTextField txtNmTindakan;
    private javax.swing.JTextField txtTglRekam;
    private javax.swing.JLabel txtTotalBiaya;
    // End of variables declaration//GEN-END:variables
}
