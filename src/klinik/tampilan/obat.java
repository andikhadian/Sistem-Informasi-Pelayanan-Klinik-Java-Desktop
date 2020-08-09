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
public class obat extends javax.swing.JInternalFrame {

    /**
     * Creates new form obat
     */
    DefaultTableModel tabel = new DefaultTableModel();
    
    public obat() {
        initComponents();
        autoNumber();
        loadData();
        txtKdObat.setEnabled(false);
        kosong();
    }
    
    public void kosong() {
        txtNmObat.setText("");
        txtStokObat.setText("");
        txtHargaObat.setText("");
        txtKetObat.setText("");
        btnTambah.setEnabled(true);
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
        autoNumber();
    }
    
    private void autoNumber() {
        try {

            java.sql.Connection line_konek = (com.mysql.jdbc.Connection)klinik.koneksi.koneksi.getDB();
            java.sql.Statement line_statemen = line_konek.createStatement();
            String query_bukaTabel = "SELECT MAX(RIGHT(kd_obat,4)) AS nomor FROM t_obat";
            java.sql.ResultSet line_result = line_statemen.executeQuery(query_bukaTabel);
            if (line_result.first() == false) {
                txtKdObat.setText("OBT0001");
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
                txtKdObat.setText("OBT" + nomor);
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
            String querry = "SELECT kd_obat, nama_obat, stok_obat, harga_obat, keterangan FROM t_obat";
            java.sql.ResultSet rs = stm.executeQuery(querry);
            tableObat.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(Exception e){
            System.out.println("Error Load Data : " + e);
        }
    }
    
    private void simpan(){
        String kd = txtKdObat.getText();
        String nm = txtNmObat.getText();
        String stok = txtStokObat.getText();
        String harga = txtHargaObat.getText();
        String ket = txtKetObat.getText();
        
       try{
           Connection conn =(Connection)klinik.koneksi.koneksi.getDB();
           java.sql.Statement stm = conn.createStatement();
           String querry ="INSERT INTO t_obat VALUES ('"+kd+"','"+nm+"', '"+stok+"', '"+harga+"','"+ket+"')";
           stm.executeUpdate(querry);
           JOptionPane.showMessageDialog(this, "Berhasil Menambahkan obat baru", "Sukses", JOptionPane.INFORMATION_MESSAGE);
       }catch(Exception e){
           JOptionPane.showMessageDialog(this, "Gagal Memasukkan Data !", "Peringatan", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
       }
    }
    
    private void ubah(){
        try{
            String kd = txtKdObat.getText();
            String nm = txtNmObat.getText();
            String stok = txtStokObat.getText();
            String harga = txtHargaObat.getText();
            String ket = txtKetObat.getText();
            
            Connection conn =(Connection)klinik.koneksi.koneksi.getDB();
            String querry ="UPDATE t_obat SET nama_obat='"+nm+"', stok_obat='"+stok+"', harga_obat='"+harga+"', keterangan='"+ket+"' WHERE kd_obat='"+kd+"'";
            com.mysql.jdbc.PreparedStatement prepare = (PreparedStatement)conn.prepareStatement(querry);
            prepare.execute();
            JOptionPane.showMessageDialog(this, "Berhasil mengubah data obat", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            btnTambah.setEnabled(true);
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Gagal Mengubah Data !", "Peringatan", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
        }
    }
    
    private void hapus(){
        int s_row = tableObat.getSelectedRow();
        if(s_row==-1){
            return;
        }
        String dataDelete = (String) tableObat.getValueAt(s_row,0);
        try{
            Connection conn = (Connection)klinik.koneksi.koneksi.getDB();
            String querry = "DELETE FROM t_obat WHERE kd_obat=?";
            java.sql.PreparedStatement stm = (PreparedStatement)conn.prepareStatement(querry);
            stm.setString(1,dataDelete);
            stm.executeUpdate();
            stm.close();
            JOptionPane.showMessageDialog(this, "Berhasil menghapus data obat", "Sukses", JOptionPane.INFORMATION_MESSAGE);
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
        String getKode = tableObat.getValueAt(tableObat.getSelectedRow(), 0).toString();
        String getNama = tableObat.getValueAt(tableObat.getSelectedRow(), 1).toString();
        String getStok = tableObat.getValueAt(tableObat.getSelectedRow(), 2).toString();
        String getHarga = tableObat.getValueAt(tableObat.getSelectedRow(), 3).toString();
        String getKet = tableObat.getValueAt(tableObat.getSelectedRow(), 4).toString();

        txtKdObat.setText(getKode);
        txtNmObat.setText(getNama);
        txtStokObat.setText(getStok);
        txtHargaObat.setText(getHarga);
        txtKetObat.setText(getKet);
    }
    
    private void cetak() {
        try {
                    String namafile = "src/klinik/laporan/laporan_obat.jasper";
                    File file = new File(namafile);
                    JasperReport jasper = (JasperReport) JRLoader.loadObject(file.getPath());
                    JasperPrint print = JasperFillManager.fillReport(jasper, null, klinik.koneksi.koneksi.getDB());
                    JasperViewer.viewReport(print,false);
                }catch (JRException ex) {
                    System.out.println("Error Laporan : " + ex);
                } catch (SQLException ex) {
            Logger.getLogger(obat.class.getName()).log(Level.SEVERE, null, ex);
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
        txtKdObat = new javax.swing.JTextField();
        txtNmObat = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnTambah = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtKetObat = new javax.swing.JTextField();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnKosong = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtStokObat = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtHargaObat = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableObat = new javax.swing.JTable();
        btnLaporan = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Obat");
        setPreferredSize(new java.awt.Dimension(1000, 616));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Form Obat"));
        jPanel1.setToolTipText("");

        jLabel1.setText("Kode Obat");

        jLabel2.setText("Nama Obat");

        btnTambah.setText("Tambah");
        btnTambah.setPreferredSize(new java.awt.Dimension(97, 48));
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        jLabel5.setText("Keterangan Obat");

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

        jLabel3.setText("Harga Obat (Rp)");

        jLabel4.setText("Stok Obat");

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
                            .addComponent(txtNmObat, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                            .addComponent(jLabel2)
                            .addComponent(txtKdObat, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                            .addComponent(jLabel5)
                            .addComponent(txtKetObat, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                            .addComponent(jLabel3)
                            .addComponent(txtStokObat, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                            .addComponent(jLabel4)
                            .addComponent(txtHargaObat))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtKdObat, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNmObat, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtStokObat, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtHargaObat, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtKetObat, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUbah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnKosong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Daftar Obat"));

        tableObat.setModel(new javax.swing.table.DefaultTableModel(
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
        tableObat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableObatMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableObat);

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
                    .addComponent(jScrollPane1)
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
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
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
        if(txtKdObat.getText().equals("")|| txtNmObat.getText().equals("")|| txtStokObat.getText().equals("") || txtHargaObat.getText().equals("")||txtKetObat.getText().equals("")){
            JOptionPane.showMessageDialog(rootPane,"Data tidak boleh ada yang kosong","Pemberitahuan",JOptionPane.WARNING_MESSAGE);
        }else{
            simpan();
        }
        loadData();
        autoNumber();
        kosong();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        if(txtKdObat.getText().equals("")|| txtNmObat.getText().equals("") || txtStokObat.getText().equals("") || txtHargaObat.getText().equals("") || txtKetObat.getText().equals("")){
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

    private void tableObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableObatMouseClicked
        klik();
    }//GEN-LAST:event_tableObatMouseClicked

    private void btnLaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLaporanActionPerformed
        cetak();
    }//GEN-LAST:event_btnLaporanActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKosong;
    private javax.swing.JButton btnLaporan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnUbah;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableObat;
    private javax.swing.JTextField txtHargaObat;
    private javax.swing.JTextField txtKdObat;
    private javax.swing.JTextField txtKetObat;
    private javax.swing.JTextField txtNmObat;
    private javax.swing.JTextField txtStokObat;
    // End of variables declaration//GEN-END:variables
}
