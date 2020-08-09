-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Aug 09, 2020 at 01:14 PM
-- Server version: 5.7.26
-- PHP Version: 7.3.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `klinik`
--

-- --------------------------------------------------------

--
-- Table structure for table `t_detail_obat_rekam_medis`
--

CREATE TABLE `t_detail_obat_rekam_medis` (
  `kd_detail_obat_rekam_medis` int(11) NOT NULL,
  `kd_rekam_medis` varchar(20) NOT NULL,
  `kd_obat` varchar(200) NOT NULL,
  `obat` varchar(200) NOT NULL,
  `harga` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `t_detail_obat_rekam_medis`
--

INSERT INTO `t_detail_obat_rekam_medis` (`kd_detail_obat_rekam_medis`, `kd_rekam_medis`, `kd_obat`, `obat`, `harga`) VALUES
(13, 'RMS0001', 'OBT0001', 'Paracetamol', 5000),
(16, 'RMS0002', 'OBT0002', 'Acarboze', 4000),
(17, 'RMS0002', 'OBT0003', 'Panadol  Merah', 5000),
(19, 'RMS0003', 'OBT0003', 'Panadol  Merah', 5000),
(24, 'RMS0004', 'OBT0001', 'Paracetamol', 5000);

-- --------------------------------------------------------

--
-- Table structure for table `t_detail_tindakan_rekam_medis`
--

CREATE TABLE `t_detail_tindakan_rekam_medis` (
  `kd_detail_tindakan_rekam_medis` int(11) NOT NULL,
  `kd_rekam_medis` varchar(20) NOT NULL,
  `kd_tindakan` varchar(20) NOT NULL,
  `tindakan` varchar(200) NOT NULL,
  `harga` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `t_detail_tindakan_rekam_medis`
--

INSERT INTO `t_detail_tindakan_rekam_medis` (`kd_detail_tindakan_rekam_medis`, `kd_rekam_medis`, `kd_tindakan`, `tindakan`, `harga`) VALUES
(34, 'RMS0001', 'TDK0001', 'Konsultasi / Premedikasi', 50000),
(35, 'RMS0001', 'TDK0003', 'Penambalan Gigi', 150000),
(39, 'RMS0002', 'TDK0001', 'Konsultasi / Premedikasi', 50000),
(41, 'RMS0003', 'TDK0001', 'Konsultasi / Premedikasi', 50000),
(46, 'RMS0004', 'TDK0001', 'Konsultasi / Premedikasi', 50000),
(47, 'RMS0004', 'TDK0002', 'Pembersihan Karang Gigi', 200000);

-- --------------------------------------------------------

--
-- Table structure for table `t_dokter`
--

CREATE TABLE `t_dokter` (
  `kd_dokter` varchar(11) NOT NULL,
  `nama_dokter` varchar(50) NOT NULL,
  `jenis_kelamin` varchar(20) NOT NULL,
  `no_hp` varchar(50) NOT NULL,
  `spesialis` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `t_dokter`
--

INSERT INTO `t_dokter` (`kd_dokter`, `nama_dokter`, `jenis_kelamin`, `no_hp`, `spesialis`) VALUES
('DR0001', 'Dr. Tirta', 'Laki-Laki', '029182192811', 'Umum'),
('DR0002', 'Dr. Soebandono Munah', 'Perempuan', '093919281312', 'Umum'),
('DR0003', 'Dr. Andiman Budiman', 'Laki-Laki', '021912048043', 'Spesialis THT'),
('DR0004', 'Dr. Soepamon', 'Laki-Laki', '092817418283', 'Spesialis Gigi'),
('DR0005', 'Dr. Troll', 'Perempuan', '081289128267', 'Spesialis Gigi');

-- --------------------------------------------------------

--
-- Table structure for table `t_kunjungan`
--

CREATE TABLE `t_kunjungan` (
  `kd_kunjungan` varchar(20) NOT NULL,
  `tgl_kunjungan` date NOT NULL,
  `kd_pasien` varchar(20) NOT NULL,
  `keluhan` varchar(200) NOT NULL,
  `kd_dokter` varchar(20) NOT NULL,
  `status_rekam_medis` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `t_kunjungan`
--

INSERT INTO `t_kunjungan` (`kd_kunjungan`, `tgl_kunjungan`, `kd_pasien`, `keluhan`, `kd_dokter`, `status_rekam_medis`) VALUES
('KNJ0001', '2020-08-07', 'PS0001', 'Gigi Geraham Bolong', 'DR0004', 'Sudah Rekam Medis'),
('KNJ0002', '2020-08-08', 'PS0004', 'muntah', 'DR0001', 'Sudah Rekam Medis'),
('KNJ0003', '2020-08-09', 'PS0002', 'Badan saya linu linu', 'DR0002', 'Sudah Rekam Medis'),
('KNJ0004', '2020-08-09', 'PS0003', 'karang gigi sudah banyak banget', 'DR0005', 'Sudah Rekam Medis');

-- --------------------------------------------------------

--
-- Table structure for table `t_obat`
--

CREATE TABLE `t_obat` (
  `kd_obat` varchar(20) NOT NULL,
  `nama_obat` varchar(50) NOT NULL,
  `stok_obat` int(11) NOT NULL,
  `harga_obat` int(11) NOT NULL,
  `keterangan` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `t_obat`
--

INSERT INTO `t_obat` (`kd_obat`, `nama_obat`, `stok_obat`, `harga_obat`, `keterangan`) VALUES
('OBT0001', 'Paracetamol', 100, 5000, 'Obat Penurun Panas'),
('OBT0002', 'Acarboze', 95, 4000, 'Obat Antidiabetes'),
('OBT0003', 'Panadol  Merah', 100, 5000, 'Obat Pusing');

-- --------------------------------------------------------

--
-- Table structure for table `t_pasien`
--

CREATE TABLE `t_pasien` (
  `kd_pasien` varchar(20) NOT NULL,
  `nama_pasien` varchar(50) NOT NULL,
  `umur` varchar(20) NOT NULL,
  `jenis_kelamin` varchar(20) NOT NULL,
  `no_hp` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `t_pasien`
--

INSERT INTO `t_pasien` (`kd_pasien`, `nama_pasien`, `umur`, `jenis_kelamin`, `no_hp`) VALUES
('PS0001', 'Bianco Akbar', '50', 'Laki-Laki', '08291923818'),
('PS0002', 'Venni Legia', '40', 'Perempuan', '07628191728'),
('PS0003', 'Indi Syafira', '23', 'Perempuan', '097261128'),
('PS0004', 'Muhammad Rifqi Subchan', '22', 'Laki-Laki', '09281827128'),
('PS0005', 'Novi', '22', 'Perempuan', '08963727');

-- --------------------------------------------------------

--
-- Table structure for table `t_petugas`
--

CREATE TABLE `t_petugas` (
  `kd_petugas` varchar(20) NOT NULL,
  `nama_petugas` varchar(50) NOT NULL,
  `jenis_kelamin` varchar(20) NOT NULL,
  `no_hp` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `t_petugas`
--

INSERT INTO `t_petugas` (`kd_petugas`, `nama_petugas`, `jenis_kelamin`, `no_hp`) VALUES
('PTG0001', 'Reyfa Refian', 'Laki-Laki', '089650682001'),
('PTG0002', 'Andikha Dian Nugraha', 'Laki-Laki', '089650682129'),
('PTG0003', 'Dhimas Islah', 'Laki-Laki', '012391293128'),
('PTG0004', 'Linda Ramadhani', 'Perempuan', '089685738219'),
('PTG0005', 'Riyan', 'Perempuan', '087877217231');

-- --------------------------------------------------------

--
-- Table structure for table `t_rekam_medis`
--

CREATE TABLE `t_rekam_medis` (
  `kd_rekam_medis` varchar(20) NOT NULL,
  `tgl_rekam_medis` date NOT NULL,
  `kd_kunjungan` varchar(20) NOT NULL,
  `hasil_diagnosa` varchar(200) NOT NULL,
  `total_biaya` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `t_rekam_medis`
--

INSERT INTO `t_rekam_medis` (`kd_rekam_medis`, `tgl_rekam_medis`, `kd_kunjungan`, `hasil_diagnosa`, `total_biaya`) VALUES
('RMS0001', '2020-08-08', 'KNJ0001', 'Tambal gigi', 205000),
('RMS0002', '2020-08-09', 'KNJ0002', 'Pasien terdiagnosa diare', 59000),
('RMS0003', '2020-08-09', 'KNJ0003', 'Kecapekan udah umur', 55000),
('RMS0004', '2020-08-09', 'KNJ0004', 'Membersihkan Karang Gigi Geraham', 255000);

-- --------------------------------------------------------

--
-- Table structure for table `t_tindakan`
--

CREATE TABLE `t_tindakan` (
  `kd_tindakan` varchar(20) NOT NULL,
  `tindakan` varchar(50) NOT NULL,
  `harga_tindakan` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `t_tindakan`
--

INSERT INTO `t_tindakan` (`kd_tindakan`, `tindakan`, `harga_tindakan`) VALUES
('TDK0001', 'Konsultasi / Premedikasi', 50000),
('TDK0002', 'Pembersihan Karang Gigi', 200000),
('TDK0003', 'Penambalan Gigi', 150000);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `t_detail_obat_rekam_medis`
--
ALTER TABLE `t_detail_obat_rekam_medis`
  ADD PRIMARY KEY (`kd_detail_obat_rekam_medis`);

--
-- Indexes for table `t_detail_tindakan_rekam_medis`
--
ALTER TABLE `t_detail_tindakan_rekam_medis`
  ADD PRIMARY KEY (`kd_detail_tindakan_rekam_medis`);

--
-- Indexes for table `t_dokter`
--
ALTER TABLE `t_dokter`
  ADD PRIMARY KEY (`kd_dokter`);

--
-- Indexes for table `t_kunjungan`
--
ALTER TABLE `t_kunjungan`
  ADD PRIMARY KEY (`kd_kunjungan`);

--
-- Indexes for table `t_obat`
--
ALTER TABLE `t_obat`
  ADD PRIMARY KEY (`kd_obat`);

--
-- Indexes for table `t_pasien`
--
ALTER TABLE `t_pasien`
  ADD PRIMARY KEY (`kd_pasien`);

--
-- Indexes for table `t_petugas`
--
ALTER TABLE `t_petugas`
  ADD PRIMARY KEY (`kd_petugas`);

--
-- Indexes for table `t_rekam_medis`
--
ALTER TABLE `t_rekam_medis`
  ADD PRIMARY KEY (`kd_rekam_medis`);

--
-- Indexes for table `t_tindakan`
--
ALTER TABLE `t_tindakan`
  ADD PRIMARY KEY (`kd_tindakan`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `t_detail_obat_rekam_medis`
--
ALTER TABLE `t_detail_obat_rekam_medis`
  MODIFY `kd_detail_obat_rekam_medis` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT for table `t_detail_tindakan_rekam_medis`
--
ALTER TABLE `t_detail_tindakan_rekam_medis`
  MODIFY `kd_detail_tindakan_rekam_medis` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=48;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
