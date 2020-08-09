/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package klinik.tampilan;

import com.mysql.jdbc.PreparedStatement;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
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
public class pasien extends javax.swing.JInternalFrame {

    /**
     * Creates new form pasien
     */
    DefaultTableModel tabel = new DefaultTableModel();
    public pasien() {
        initComponents();
        autoNumber();
        loadData();
        txtKdPasien.setEnabled(false);
        kosong();
    }
    
    public void kosong() {
        txtNmPasien.setText("");
        txtUmurPasien.setText("");
        txtHpPasien.setText("");
        btnTambah.setEnabled(true);
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
        autoNumber();
    }
    
    private void autoNumber() {
        try {

            java.sql.Connection line_konek = (com.mysql.jdbc.Connection)klinik.koneksi.koneksi.getDB();
            java.sql.Statement line_statemen = line_konek.createStatement();
            String query_bukaTabel = "SELECT MAX(RIGHT(kd_pasien,4)) AS nomor FROM t_pasien";
            java.sql.ResultSet line_result = line_statemen.executeQuery(query_bukaTabel);
            if (line_result.first() == false) {
                txtKdPasien.setText("PS0001");
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
                txtKdPasien.setText("PS" + nomor);
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
            String querry = "SELECT kd_pasien, nama_pasien, umur, jenis_kelamin, no_hp FROM t_pasien";
            java.sql.ResultSet rs = stm.executeQuery(querry);
            tablePasien.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(Exception e){
            System.out.println("Error Load Data : " + e);
        }
    }
    
    private void simpan(){
        String kd = txtKdPasien.getText();
        String nm = txtNmPasien.getText();
        String umur = txtUmurPasien.getText();
        String hp = txtHpPasien.getText();
        String jk = cmbJenisKelamin.getSelectedItem().toString();
        
       try{
           Connection conn =(Connection)klinik.koneksi.koneksi.getDB();
           java.sql.Statement stm = conn.createStatement();
           String querry ="INSERT INTO t_pasien VALUES ('"+kd+"','"+nm+"', '"+umur+"', '"+jk+"','"+hp+"')";
           stm.executeUpdate(querry);
           JOptionPane.showMessageDialog(this, "Berhasil Menambahkan pasien baru", "Sukses", JOptionPane.INFORMATION_MESSAGE);
       }catch(Exception e){
           JOptionPane.showMessageDialog(this, "Gagal Memasukkan Data !", "Peringatan", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
       }
    }
    
    private void ubah(){
        try{
            String kd = txtKdPasien.getText();
            String nm = txtNmPasien.getText();
            String umur = txtUmurPasien.getText();
            String hp = txtHpPasien.getText();
            String jk = cmbJenisKelamin.getSelectedItem().toString();
            
            Connection conn =(Connection)klinik.koneksi.koneksi.getDB();
            String querry ="UPDATE t_pasien SET nama_pasien='"+nm+"', umur='"+umur+"', jenis_kelamin='"+jk+"', no_hp='"+hp+"' WHERE kd_pasien='"+kd+"'";
            com.mysql.jdbc.PreparedStatement prepare = (PreparedStatement)conn.prepareStatement(querry);
            prepare.execute();
            JOptionPane.showMessageDialog(this, "Berhasil mengubah data pasien", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            btnTambah.setEnabled(true);
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Gagal Mengubah Data !", "Peringatan", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
        }
    }
    
    private void hapus(){
        int s_row = tablePasien.getSelectedRow();
        if(s_row==-1){
            return;
        }
        String dataDelete = (String) tablePasien.getValueAt(s_row,0);
        try{
            Connection conn = (Connection)klinik.koneksi.koneksi.getDB();
            String querry = "DELETE FROM t_pasien WHERE kd_pasien=?";
            java.sql.PreparedStatement stm = (PreparedStatement)conn.prepareStatement(querry);
            stm.setString(1,dataDelete);
            stm.executeUpdate();
            stm.close();
            JOptionPane.showMessageDialog(this, "Berhasil menghapus data pasien", "Sukses", JOptionPane.INFORMATION_MESSAGE);
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
        String getKode = tablePasien.getValueAt(tablePasien.getSelectedRow(), 0).toString();
        String getNama = tablePasien.getValueAt(tablePasien.getSelectedRow(), 1).toString();
        String getUmur = tablePasien.getValueAt(tablePasien.getSelectedRow(), 2).toString();
        String getJk = tablePasien.getValueAt(tablePasien.getSelectedRow(), 3).toString();
        String getHp = tablePasien.getValueAt(tablePasien.getSelectedRow(), 4).toString();

        txtKdPasien.setText(getKode);
        txtNmPasien.setText(getNama);
        txtUmurPasien.setText(getUmur);
        txtHpPasien.setText(getHp);
        cmbJenisKelamin.getModel().setSelectedItem(getJk);
    }
    
    private void cetak() {
        try {
                    String namafile = "src/klinik/laporan/laporan_pasien.jasper";
                    File file = new File(namafile);
                    JasperReport jasper = (JasperReport) JRLoader.loadObject(file.getPath());
                    JasperPrint print = JasperFillManager.fillReport(jasper, null, klinik.koneksi.koneksi.getDB());
                    JasperViewer.viewReport(print,false);
                }catch (JRException ex) {
                    System.out.println("Error Laporan : " + ex);
                } catch (SQLException ex) {
            Logger.getLogger(pasien.class.getName()).log(Level.SEVERE, null, ex);
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtKdPasien = new javax.swing.JTextField();
        txtNmPasien = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnTambah = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtHpPasien = new javax.swing.JTextField();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnKosong = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        cmbJenisKelamin = new javax.swing.JComboBox<>();
        txtUmurPasien = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablePasien = new javax.swing.JTable();
        btnLaporan = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Pasien");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Form Pasien"));
        jPanel1.setToolTipText("");

        jLabel1.setText("Kode Pasien");

        jLabel2.setText("Nama Pasien");

        btnTambah.setText("Tambah");
        btnTambah.setPreferredSize(new java.awt.Dimension(97, 48));
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        jLabel5.setText("Nomor Handphone Pasien");

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

        jLabel3.setText("Jenis Kelamin");

        cmbJenisKelamin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Laki-Laki", "Perempuan" }));

        jLabel4.setText("Umur Pasien");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnTambah, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUbah, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnHapus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnKosong, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1)
                            .addComponent(txtNmPasien, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                            .addComponent(jLabel2)
                            .addComponent(txtKdPasien, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                            .addComponent(jLabel5)
                            .addComponent(txtHpPasien, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                            .addComponent(jLabel3)
                            .addComponent(cmbJenisKelamin, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtUmurPasien, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                            .addComponent(jLabel4))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtKdPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNmPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUmurPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbJenisKelamin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtHpPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUbah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnKosong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Daftar Pasien"));

        tablePasien.setModel(new javax.swing.table.DefaultTableModel(
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
        tablePasien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablePasienMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablePasien);

        btnLaporan.setText("Cetak Laporan");
        btnLaporan.setPreferredSize(new java.awt.Dimension(97, 48));
        btnLaporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLaporanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnLaporan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 558, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        if(txtKdPasien.getText().equals("")|| txtNmPasien.getText().equals("")|| txtUmurPasien.getText().equals("")||txtHpPasien.getText().equals("")){
            JOptionPane.showMessageDialog(rootPane,"Data tidak boleh ada yang kosong","Pemberitahuan",JOptionPane.WARNING_MESSAGE);
        }else{
            simpan();
        }
        loadData();
        autoNumber();
        kosong();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        if(txtKdPasien.getText().equals("")|| txtNmPasien.getText().equals("") || txtUmurPasien.getText().equals("") || txtHpPasien.getText().equals("")){
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

    private void tablePasienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablePasienMouseClicked
        klik();
    }//GEN-LAST:event_tablePasienMouseClicked

    private void btnLaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLaporanActionPerformed
        cetak();
    }//GEN-LAST:event_btnLaporanActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKosong;
    private javax.swing.JButton btnLaporan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnUbah;
    private javax.swing.JComboBox<String> cmbJenisKelamin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablePasien;
    private javax.swing.JTextField txtHpPasien;
    private javax.swing.JTextField txtKdPasien;
    private javax.swing.JTextField txtNmPasien;
    private javax.swing.JTextField txtUmurPasien;
    // End of variables declaration//GEN-END:variables
}
