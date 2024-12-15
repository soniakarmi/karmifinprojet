-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : sam. 30 nov. 2024 à 11:29
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `backendsonia`
--

-- --------------------------------------------------------

--
-- Structure de la table `classe`
--

CREATE TABLE `classe` (
  `id` bigint(20) NOT NULL,
  `nom` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `classe`
--

INSERT INTO `classe` (`id`, `nom`) VALUES
(2, '2 twin 30'),
(3, '2 twin 3'),
(4, '2 twin 7'),
(5, '2 embarque 8'),
(6, '3 se 5'),
(52, 'moneammmmmmmm'),
(53, 'sonia'),
(152, '3 twin 5'),
(153, '3 twin 6');

-- --------------------------------------------------------

--
-- Structure de la table `classe_seq`
--

CREATE TABLE `classe_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `classe_seq`
--

INSERT INTO `classe_seq` (`next_val`) VALUES
(251);

-- --------------------------------------------------------

--
-- Structure de la table `cours`
--

CREATE TABLE `cours` (
  `id` bigint(20) NOT NULL,
  `datedebut` date DEFAULT NULL,
  `datefin` date DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `titre` varchar(255) DEFAULT NULL,
  `classe` bigint(20) DEFAULT NULL,
  `enseignant_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `cours`
--

INSERT INTO `cours` (`id`, `datedebut`, `datefin`, `description`, `titre`, `classe`, `enseignant_id`) VALUES
(1, '2024-06-01', '2024-06-30', ' cour java', 'ukjyhtgrfedjuytgrfe', 4, 4),
(2, '2024-06-01', '2024-06-30', 'description ', 'bonjour ', 2, 29),
(5, '2024-06-01', '2024-06-30', ' cour java', 'ukjyhtgrfedjuytgrfe', 2, 4),
(52, '2024-06-01', '2024-06-30', ' cour java', 'ukjyhtgrfedjuytgrfe', 2, 4),
(152, '2024-06-01', '2024-06-30', '.net', 'bonjour', 2, 29),
(153, '2024-06-01', '2024-06-30', '.net', 'bonjour', 2, 29);

-- --------------------------------------------------------

--
-- Structure de la table `cours_seq`
--

CREATE TABLE `cours_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `cours_seq`
--

INSERT INTO `cours_seq` (`next_val`) VALUES
(251);

-- --------------------------------------------------------

--
-- Structure de la table `note`
--

CREATE TABLE `note` (
  `id` bigint(20) NOT NULL,
  `note` float NOT NULL,
  `etudiant` bigint(20) DEFAULT NULL,
  `quiz_form` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `notification`
--

CREATE TABLE `notification` (
  `id` bigint(20) NOT NULL,
  `notification` varchar(255) DEFAULT NULL,
  `sent_on` enum('EMAIL','SMS') DEFAULT NULL,
  `type` enum('CONFIRMATION','CONTACT') DEFAULT NULL,
  `etudiant` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `notification_seq`
--

CREATE TABLE `notification_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `notification_seq`
--

INSERT INTO `notification_seq` (`next_val`) VALUES
(1);

-- --------------------------------------------------------

--
-- Structure de la table `paiement`
--

CREATE TABLE `paiement` (
  `id` bigint(20) NOT NULL,
  `date` datetime(6) DEFAULT NULL,
  `montant_par_tranche` double DEFAULT NULL,
  `montant_paye` double DEFAULT NULL,
  `montant_restant` double DEFAULT NULL,
  `montant_total` double DEFAULT NULL,
  `nombre_de_tranches` int(11) DEFAULT NULL,
  `status` enum('ACCEPTE','COMPLET','INCOMPLETE','REFUSE') DEFAULT NULL,
  `etudiant` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `paiement`
--

INSERT INTO `paiement` (`id`, `date`, `montant_par_tranche`, `montant_paye`, `montant_restant`, `montant_total`, `nombre_de_tranches`, `status`, `etudiant`) VALUES
(1, NULL, NULL, 0, 0, 0, 0, 'INCOMPLETE', 11),
(2, NULL, NULL, 0, 0, 0, 0, 'INCOMPLETE', 15),
(3, NULL, NULL, 0, 0, 0, 0, 'INCOMPLETE', 23);

-- --------------------------------------------------------

--
-- Structure de la table `quiz_form`
--

CREATE TABLE `quiz_form` (
  `id` bigint(20) NOT NULL,
  `coeif` float NOT NULL,
  `last_updated_at` date DEFAULT NULL,
  `type` enum('DS','EXAMEN') DEFAULT NULL,
  `support_cours_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `quiz_proposition`
--

CREATE TABLE `quiz_proposition` (
  `id` bigint(20) NOT NULL,
  `created_at` date DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `quiz_question` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `quiz_proposition_seq`
--

CREATE TABLE `quiz_proposition_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `quiz_proposition_seq`
--

INSERT INTO `quiz_proposition_seq` (`next_val`) VALUES
(1);

-- --------------------------------------------------------

--
-- Structure de la table `quiz_question`
--

CREATE TABLE `quiz_question` (
  `id` bigint(20) NOT NULL,
  `created_at` date DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `points` int(11) NOT NULL,
  `response_correct_id` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `quiz_form_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `quiz_question_seq`
--

CREATE TABLE `quiz_question_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `quiz_question_seq`
--

INSERT INTO `quiz_question_seq` (`next_val`) VALUES
(1);

-- --------------------------------------------------------

--
-- Structure de la table `quiz_response`
--

CREATE TABLE `quiz_response` (
  `id` bigint(20) NOT NULL,
  `etudiant` bigint(20) DEFAULT NULL,
  `question` bigint(20) DEFAULT NULL,
  `proposition` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `quiz_response_seq`
--

CREATE TABLE `quiz_response_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `quiz_response_seq`
--

INSERT INTO `quiz_response_seq` (`next_val`) VALUES
(1);

-- --------------------------------------------------------

--
-- Structure de la table `reclamation`
--

CREATE TABLE `reclamation` (
  `id` bigint(20) NOT NULL,
  `date` date DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `sujet` varchar(255) DEFAULT NULL,
  `etudiant` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `reclamation`
--

INSERT INTO `reclamation` (`id`, `date`, `description`, `sujet`, `etudiant`) VALUES
(1, '2024-11-01', 'date n\'est pas correct', 'note examun ', 11),
(2, '2024-11-06', 'date n\'est pas correct', 'date DS ', 15),
(52, '2024-11-06', 'date n\'est pas correct', 'note ds ', 15),
(102, '2024-11-17', 'date n\'est pas correct', 'note ds ', 22),
(152, '2024-11-22', 'date n\'est pas correct', 'note  ', 23),
(202, '2024-11-26', 'date n\'est pas correct', 'note  ', 26),
(252, '2024-11-26', 'date n\'est pas correct', 'note  ', 27),
(302, '2024-11-26', 'date n\'est pat', 'note  ', 27);

-- --------------------------------------------------------

--
-- Structure de la table `reclamation_seq`
--

CREATE TABLE `reclamation_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `reclamation_seq`
--

INSERT INTO `reclamation_seq` (`next_val`) VALUES
(451);

-- --------------------------------------------------------

--
-- Structure de la table `support_cours`
--

CREATE TABLE `support_cours` (
  `dtype` varchar(31) NOT NULL,
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `type_quiz` enum('DS','EXAMEN') DEFAULT NULL,
  `type_support_cours` varchar(255) DEFAULT NULL,
  `type` tinyint(4) DEFAULT NULL,
  `cours_id` bigint(20) DEFAULT NULL,
  `enseignant_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `support_cours`
--

INSERT INTO `support_cours` (`dtype`, `id`, `created_at`, `description`, `image`, `type_quiz`, `type_support_cours`, `type`, `cours_id`, `enseignant_id`) VALUES
('SupportCours', 1, '2024-11-29 21:45:23.000000', 'descriptionnnnnnnnnnnnnnnnnn', 'téléchargement (3)277.jpg', NULL, 'typeSupportCourssssssssssss', NULL, 5, 33);

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

CREATE TABLE `utilisateur` (
  `utilisateur` varchar(31) NOT NULL,
  `id` bigint(20) NOT NULL,
  `adresse` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `enable` bit(1) NOT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `prenom` varchar(255) DEFAULT NULL,
  `role` enum('ADMIN','ENSEIGNANT','ETUDIANT','PARENT') DEFAULT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  `accepted` bit(1) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `classe` bigint(20) DEFAULT NULL,
  `parent` bigint(20) DEFAULT NULL,
  `etudiant` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `utilisateur`
--

INSERT INTO `utilisateur` (`utilisateur`, `id`, `adresse`, `email`, `enable`, `nom`, `password`, `photo`, `prenom`, `role`, `telephone`, `accepted`, `code`, `classe`, `parent`, `etudiant`) VALUES
('Utilisateur', 1, 'addess', 'sonia@gmail.com', b'1', 'sonia', '$2a$10$I3MAYCNkKuKv5to0MVlQU.SCQaQIK9OUl7nYZ1glHjgIZ35nx8GjS', NULL, 'sonia', 'ADMIN', '123456', NULL, NULL, NULL, NULL, NULL),
('Etudiant', 2, 'sfax', 'karmisonia4@gmail.com', b'0', 'lamin', '$2a$10$I9dnK4WI19bgP7XTT.Yhxup19wPiyi6CVIr5xUu5PPS.uLgunNnzW', 'téléchargement (2)792.jpg', 'abdouli', 'ETUDIANT', '12365', b'0', NULL, NULL, NULL, NULL),
('Enseignant', 3, 'sfax', 'saber@gmail.com', b'0', 'saber', '$2a$10$/o8o3ZA4koar7h4FqsWsYO31YxjDergDuXiyTAmDS56B.dcA3DDdG', 'téléchargement (2)120.jpg', 'abdouli', 'ENSEIGNANT', '12365', NULL, NULL, NULL, NULL, NULL),
('Enseignant', 4, 'sfax', 'samiakarmi@gmail.com', b'0', 'saber', '$2a$10$nKmgNiYedIlw8msVNzWsxuNuuXEfngoxOeRnzCLhtwIhKAh2RvOFW', 'téléchargement (2)442.jpg', 'abdouli', 'ENSEIGNANT', '12365', NULL, NULL, NULL, NULL, NULL),
('Etudiant', 5, 'sfax', 'aliali@gmail.com', b'0', 'saber', '$2a$10$o8.88LfZUJDXXx8cJ3twWeZ.sQ2ZdZBfsdBRR80K0R2/KfmGrtMv6', 'téléchargement (2)818.jpg', 'abdouli', 'ETUDIANT', '12365', b'0', NULL, NULL, NULL, NULL),
('Etudiant', 6, 'sfax', 'alikarmi@hotmail.tn', b'1', 'saber', '$2a$10$SCCoJ7Sn5QWzAHOdGc1G/e/X3jO01Nlds4hKTC0cQwmJXA7olPIbq', 'téléchargement (2)872.jpg', 'abdouli', 'ETUDIANT', '12365', b'0', NULL, NULL, NULL, NULL),
('Etudiant', 7, 'sedrfghj', 'gmarcamoneam@gmail.com', b'1', 'moneam', '$2a$10$4yxGuBIXqTHRyhransvtu.aTrmISNgi7/AZV5cbPe8ZSjSp/0e1pG', 'images988.jpg', 'moneam', 'ETUDIANT', '21354', b'0', NULL, NULL, NULL, NULL),
('Administration', 8, '', 'aymen@gmail.com', b'0', '', '$2a$10$UW/gAMwjlZgLe2EiIcNzHuVYAq28EMIo6oDP0jeB7C..pueg.2NXS', NULL, '', 'ADMIN', '12', NULL, NULL, NULL, NULL, NULL),
('Enseignant', 9, 'sfax', 'aymen1@gmail.com', b'1', 'aymen', '$2a$10$JigrOc4DQ8RdWATer37x0uy6SfXaIFTC8sxwB8jvCkxXj5xkabu56', 'téléchargement (2)869.jpg', 'aymen', 'ENSEIGNANT', '123456', NULL, NULL, NULL, NULL, NULL),
('Parent', 10, 'Nabeul', 'Parent@gmail.com', b'0', 'Parent', '$2a$10$x.WYzdi71goPeVZBjbQozOSBbUoUkdtzShIvQl0IvT8CCLxR7KZR2', 'téléchargement (3)154.jpg', 'Parent', 'PARENT', '124587963', NULL, NULL, NULL, NULL, NULL),
('Etudiant', 11, 'sfax', 'sarasara@hotmail.tn', b'0', 'sara', '$2a$10$iAwxbnTBK7vF7ivhAsSe1e/zafArYw//zaZHMfgizIuYYOCL5Mcaa', 'téléchargement (2)695.jpg', 'ajmi', 'ETUDIANT', '12365', b'1', 'ETU98036', 6, NULL, NULL),
('Parent', 12, 'sfax', 'karmiali@hotmail.tn', b'1', 'sara', '$2a$10$x8v0dcnBS8./KrOE5zpoXuxHHs7SXDII86FxlXVkGdXYpk85Tnzcq', 'téléchargement (3)857.jpg', 'ajmi', 'PARENT', '12365', NULL, NULL, NULL, NULL, NULL),
('Parent', 13, 'sfax', 'karmia@hotmail.tn', b'0', 'hatem', '$2a$10$T6VHdhmBxx2RpAvSO9.iV.KQ5UVm/gNaWlByYpD4../0sBcN2GAma', 'téléchargement (3)379.jpg', 'bouzidi', 'PARENT', '12365', NULL, NULL, NULL, NULL, NULL),
('Etudiant', 14, 'sfax', 'hatemkarmia@hotmail.tn', b'1', 'hatem', '$2a$10$FWb9nLECOrtrFpSRWjAypuc4gNwE8jkuQ7ateydZauLGhXBNjfGjO', 'téléchargement (3)577.jpg', 'bouzidi', 'ETUDIANT', '12365', b'0', NULL, NULL, NULL, NULL),
('Etudiant', 15, 'sfax', 'sana@gmail.com', b'1', 'sana', '$2a$10$NpxEFe335IW64l4AV2bbKuV5kTrAiKFF/g1yYLDooWaomWza9GE3.', 'téléchargement (3)450.jpg', 'ajmi', 'ETUDIANT', '12365', b'1', 'ETU98576', 4, NULL, NULL),
('Etudiant', 16, 'sfax', 'sanaajmi@gmail.tn', b'0', 'sana', '$2a$10$bf7/JMhCqsUkzqUSQ6Z/UO8f5xEAu.iKeQjNBDy157gSkHp8HY/vS', 'téléchargement (3)503.jpg', 'ajmi', 'ETUDIANT', '12365', b'0', NULL, NULL, NULL, NULL),
('Enseignant', 17, 'sfax', 'medali@gmail.com', b'1', 'sana', '$2a$10$OKcFT1s12ghLzJ1bmnODjOz5sm9iq9vc067qP4OCzD/WGRBh.bcSi', 'téléchargement (3)140.jpg', 'ajmi', 'ENSEIGNANT', '12365', NULL, NULL, NULL, NULL, NULL),
('Enseignant', 18, 'sfax', 'bassem@gmail.com', b'0', 'sana', '$2a$10$zTWcv6CjwthrJNsQUUcMNuxXq9awpe0WeFSH.nJvRqS.iMcTc2C.q', 'téléchargement (3)490.jpg', 'ajmi', 'ENSEIGNANT', '12365', NULL, NULL, NULL, NULL, NULL),
('Enseignant', 19, 'sfax', 'bassem@gmail.tn', b'0', 'bassem', '$2a$10$T8xdbJXyN8CcE.X9mDQ2kexaZJI4KVtlJqxCmigfY7czgcvkGDEDm', 'téléchargement (6)956.jpg', 'abdouli', 'ENSEIGNANT', '12365', NULL, NULL, NULL, NULL, NULL),
('Enseignant', 20, 'sfax', 'karmioswa@gmail.tn', b'0', 'oswa', '$2a$10$i1o6nwTPuqXBzagsNN4ljePVizr7RfW1K0se3RqrvOfGWUgkqDxf6', 'téléchargement (2)974.jpg', 'karmi', 'ENSEIGNANT', '12365', NULL, NULL, NULL, NULL, NULL),
('Etudiant', 21, 'sfax', 'karmiwahib@gmail.tn', b'0', 'wahiba', '$2a$10$fHmrDbrlbiPnX6yI5k9t7ehlMBk4Ekv83Yk.Kj2/tXXCzx9POUPf.', 'téléchargement (2)190.jpg', 'souci', 'ETUDIANT', '12365', b'0', NULL, NULL, NULL, NULL),
('Etudiant', 22, 'sfax', 'adeladel@gmail.com', b'0', 'adel', '$2a$10$.0h5JE6NP1ei/OoxN905uOEeWQvzCdJjNCUGoaHexF/kU3z1MAdbm', 'téléchargement (5)474.jpg', 'souci', 'ETUDIANT', '12365', b'0', NULL, NULL, NULL, NULL),
('Etudiant', 23, 'sfax', 'abdouli@gmail.com', b'1', 'adel', '$2a$10$DI0hpKGdmB1UBDFcg81xoeeIHctDO1EIlSBNAKsf.fxdRk6M/Mmp.', 'téléchargement (6)933.jpg', 'abdouli', 'ETUDIANT', '12365', b'1', 'ETU62045', 4, NULL, NULL),
('Enseignant', 24, 'sfax', 'amineabdouli@gmail.com', b'1', 'amine', '$2a$10$Z8TSfOCHZty9kLTnHSQNCeGsctgpi4f9dPAPWv3b6y9Oc7/InGfP.', 'téléchargement (6)613.jpg', 'abdouli', 'ENSEIGNANT', '12365', NULL, NULL, NULL, NULL, NULL),
('Etudiant', 25, 'sfax', 'alikarmi@gmail.com', b'0', 'amine', '$2a$10$yCUv/k45/Ddh5TMk7pwhpuHu7J14UsyXYrV8mOMsn.Om4sLKA/mxy', 'téléchargement (6)750.jpg', 'abdouli', 'ETUDIANT', '12365', b'0', NULL, NULL, NULL, NULL),
('Etudiant', 26, 'sfax', 'arwakarmi@gmail.tn', b'0', 'arwa', '$2a$10$DHwMhNfsfhUZvVTx5s4q4uGpX3ck/vZD3ksuFAtMGwHxWbo9gi0zC', 'téléchargement (6)262.jpg', 'rebhi', 'ETUDIANT', '12365', b'0', NULL, NULL, NULL, NULL),
('Etudiant', 27, 'sfax', 'arwakarmi@gmail.com', b'0', 'arwa', '$2a$10$j/SSCQINn8cHTmJNL4NL4ODOIT466stGlzgKDJqDtv6m.dYN62x5e', 'téléchargement (6)230.jpg', 'rebhi', 'ETUDIANT', '12365', b'0', NULL, NULL, NULL, NULL),
('Etudiant', 28, 'sfax', 'wakarmi@gmail.com', b'1', 'arwa', '$2a$10$yE21suJS7q40luFTQWG1COBvRx21/QYLTO9ZHWcvEE5e2CIyiaxby', 'téléchargement (6)820.jpg', 'rebhi', 'ETUDIANT', '12365', b'0', NULL, NULL, NULL, NULL),
('Enseignant', 29, 'sfax', 'rebhirebhi@gmail.com', b'0', 'arwa', '$2a$10$Lov0Xtn28omuP0pJBb84qexLwieIWdWu81vmbOXjZFz3pmaf2wd4m', 'téléchargement (6)787.jpg', 'rebhi', 'ENSEIGNANT', '12365', NULL, NULL, NULL, NULL, NULL),
('Etudiant', 30, 'sfax', 'bassem.ajmi@gmail.com', b'0', 'bassem', '$2a$10$VgU4iE4cN4Xrbd6KTYBOweSidHNyp2/wOEHskN55LJSNDYyck7Aly', 'téléchargement (5)726.jpg', 'rebhi', 'ETUDIANT', '12365', b'0', NULL, NULL, NULL, NULL),
('Etudiant', 31, 'sfax', 'mouna.ajmi@gmail.com', b'0', 'mouna', '$2a$10$jM7/14p8xeA2KWt0Pt9QYeGIfNROqAP9j2KY5cTrHcPZWIRoj5cVO', 'téléchargement (5)59.jpg', 'rebhi', 'ETUDIANT', '12365', b'0', NULL, NULL, NULL, NULL),
('Parent', 32, 'sfax', 'lamine@gmail.com', b'0', 'lamine', '$2a$10$K6kck.FepQQJnHMAMEN9KupJAu0vPzxzrJbjx6FKSZ6WvdrMuJcuS', 'téléchargement (5)878.jpg', 'rebhi', 'PARENT', '12365', NULL, NULL, NULL, NULL, NULL),
('Enseignant', 33, 'sfax', 'zohne@gmail.com', b'0', 'zohra', '$2a$10$SilYcW0ksv/thVeP5gp5Qu.hHPkXxLWrX0EDQHsZTuSUcYCZlIskK', 'téléchargement (5)995.jpg', 'rebhi', 'ENSEIGNANT', '12365', NULL, NULL, NULL, NULL, NULL),
('Parent', 34, 'sfax', 'medajmi@gmail.com', b'0', 'med', '$2a$10$0Jy4If9dNked/iK2sh/.4.XjKvMy5vWsYyC1oyqlVEr5T0HLu5.vi', 'téléchargement (5)46.jpg', 'ajmi', 'PARENT', '12365', NULL, NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `verification_token`
--

CREATE TABLE `verification_token` (
  `id` bigint(20) NOT NULL,
  `expiration_time` datetime(6) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `verification_token`
--

INSERT INTO `verification_token` (`id`, `expiration_time`, `token`, `user_id`) VALUES
(1, '2024-10-29 22:17:12.000000', '85ee1652-35f4-445a-8977-1ec76bbfc3cf', 2),
(2, '2024-10-29 22:18:01.000000', 'effb7b2a-bab6-472d-b136-bd32f0fa23a9', 3),
(3, '2024-10-29 22:18:45.000000', '1d74d7cb-0767-425a-851d-b0253c475351', 4),
(4, '2024-10-29 22:29:01.000000', 'e5215ef2-4786-4211-81c8-ad48095137e9', 5),
(5, '2024-10-29 22:30:04.000000', '19115276-ed89-4eda-92d1-ff8567ccf263', 6),
(6, '2024-10-30 00:09:12.000000', 'a0b45fac-a47a-47f2-a15f-039a116fb9b7', 7),
(7, '2024-10-30 00:21:15.000000', 'f2dda785-f356-4939-a740-466462cf3365', 8),
(8, '2024-10-30 00:26:22.000000', '1bddba28-2c2b-4093-9136-981ea22a5f13', 9),
(9, '2024-10-30 00:30:40.000000', '9cc43257-23da-4b78-867d-4e859a986fa2', 10),
(10, '2024-11-01 21:51:24.000000', '30e04ccc-a1b8-43b3-bc52-effcf49810ed', 11),
(11, '2024-11-04 17:02:52.000000', '2054c170-d7bf-4c5c-8ec1-4218cfcffe3d', 12),
(12, '2024-11-04 21:06:31.000000', '958abdff-7a30-4e06-b7aa-0e05e5a8865e', 13),
(13, '2024-11-04 21:06:59.000000', '156d1f83-335b-4d8b-b003-c9c03af4cd44', 14),
(14, '2024-11-06 20:47:49.000000', '32bf45a2-ede6-443d-849a-baa628332e8e', 15),
(15, '2024-11-06 21:12:09.000000', 'a4b7e4a6-5bfb-4b10-8366-11ee3a0307b7', 16),
(16, '2024-11-06 21:15:35.000000', 'ac12f761-0a26-4a6b-880f-70cab781fe6b', 17),
(17, '2024-11-13 20:20:26.000000', 'cc338aee-0b7f-403e-82b6-5447fee6f3aa', 18),
(18, '2024-11-13 20:55:31.000000', '3f5248b2-94bc-428c-bb7c-3508aeca6ed1', 19),
(19, '2024-11-13 20:57:04.000000', '3cd26cee-b1d7-4c74-85fc-dc9e443cf7d3', 20),
(20, '2024-11-13 20:58:20.000000', '53f82780-6b19-446f-9dfd-bb92ca8b7688', 21),
(21, '2024-11-13 20:59:28.000000', '388a0d47-875e-4b43-837d-a19f1de63f9c', 22),
(22, '2024-11-22 16:01:31.000000', 'e8658c99-158e-4727-adbd-1b26896b3048', 23),
(23, '2024-11-22 16:07:53.000000', 'cecb80de-6eb8-4564-93ae-75dcbf82d808', 24),
(24, '2024-11-24 22:11:04.000000', 'b8fc7c1d-e1cd-4ae6-825f-c2a3c681eeef', 25),
(25, '2024-11-26 20:23:30.000000', 'fb4d09c0-3a59-446b-8ca7-8efe6fa7adc2', 26),
(26, '2024-11-26 21:56:50.000000', '5ac17150-a548-4a2f-bb4e-97889dfdfd32', 27),
(27, '2024-11-26 23:40:58.000000', 'dc61c390-441f-4427-8a22-59d988e119f5', 28),
(28, '2024-11-27 21:04:33.000000', '3199d130-5c25-4cb3-9468-9ca59c9b843a', 29),
(29, '2024-11-27 21:14:15.000000', 'abc4b32b-bbba-40ba-bd7c-7704b403101e', 30),
(30, '2024-11-27 22:00:56.000000', 'bc329425-db0c-4150-a2ba-3fbd9439a282', 31),
(31, '2024-11-27 22:09:49.000000', '66d475a4-a28f-4b4a-98a1-d9284bd7de1f', 32),
(32, '2024-11-28 20:39:10.000000', '07404588-b63c-4b95-a72d-f2cbf09a3298', 33),
(33, '2024-11-28 21:24:45.000000', '91d99791-c03b-4ce0-b1cb-248f6e62d916', 34);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `classe`
--
ALTER TABLE `classe`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `cours`
--
ALTER TABLE `cours`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKpj55irmjse2hsfjrfd9l724sh` (`classe`),
  ADD KEY `FKg8whe2hucmqvqofy84e109npt` (`enseignant_id`);

--
-- Index pour la table `note`
--
ALTER TABLE `note`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKo0pbpqbdi7635n9u87y1tn1ke` (`quiz_form`),
  ADD KEY `FK392ov394fkecb68gwg072u2v1` (`etudiant`);

--
-- Index pour la table `notification`
--
ALTER TABLE `notification`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK1f1bu1cfyn2r3acihyfmy6mp8` (`etudiant`);

--
-- Index pour la table `paiement`
--
ALTER TABLE `paiement`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK44wofahyjxe5pc0l2iwif6nwe` (`etudiant`);

--
-- Index pour la table `quiz_form`
--
ALTER TABLE `quiz_form`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKhroc6eefkhschl6lo1wdmmkvv` (`support_cours_id`);

--
-- Index pour la table `quiz_proposition`
--
ALTER TABLE `quiz_proposition`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK4rfnns0mi6e0rnrgipup5k8m3` (`quiz_question`);

--
-- Index pour la table `quiz_question`
--
ALTER TABLE `quiz_question`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKp0p77bvjfmq0lac20jtcte0gr` (`quiz_form_id`);

--
-- Index pour la table `quiz_response`
--
ALTER TABLE `quiz_response`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK9cgjur228byqy6cmhwlgne7r9` (`etudiant`),
  ADD KEY `FKoq3dndstt99fyh3ggnvhswdt7` (`question`),
  ADD KEY `FK597vf1qly7602yh35p3o4oaig` (`proposition`);

--
-- Index pour la table `reclamation`
--
ALTER TABLE `reclamation`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK534vqoh6tsbl2u4cfr51tptj3` (`etudiant`);

--
-- Index pour la table `support_cours`
--
ALTER TABLE `support_cours`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK274fj851wlt1q1q5y28jjyi9l` (`cours_id`),
  ADD KEY `FK35bfqvnh0wgm5i0ewl6a73bw3` (`enseignant_id`);

--
-- Index pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKb18eaxk19i496geyfrlph0kty` (`code`),
  ADD UNIQUE KEY `UKoo275nyijgqamv3iov5nhthnx` (`etudiant`),
  ADD KEY `FK9goonys6bmu8wnxcqiaipafni` (`classe`),
  ADD KEY `FKnjr7pd4ym4xrqw0t8aaupbp23` (`parent`);

--
-- Index pour la table `verification_token`
--
ALTER TABLE `verification_token`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKq6jibbenp7o9v6tq178xg88hg` (`user_id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `note`
--
ALTER TABLE `note`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `paiement`
--
ALTER TABLE `paiement`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `quiz_form`
--
ALTER TABLE `quiz_form`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `support_cours`
--
ALTER TABLE `support_cours`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35;

--
-- AUTO_INCREMENT pour la table `verification_token`
--
ALTER TABLE `verification_token`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `cours`
--
ALTER TABLE `cours`
  ADD CONSTRAINT `FKg8whe2hucmqvqofy84e109npt` FOREIGN KEY (`enseignant_id`) REFERENCES `utilisateur` (`id`),
  ADD CONSTRAINT `FKpj55irmjse2hsfjrfd9l724sh` FOREIGN KEY (`classe`) REFERENCES `classe` (`id`);

--
-- Contraintes pour la table `note`
--
ALTER TABLE `note`
  ADD CONSTRAINT `FK392ov394fkecb68gwg072u2v1` FOREIGN KEY (`etudiant`) REFERENCES `utilisateur` (`id`),
  ADD CONSTRAINT `FKkfte31sfgx7qi97wron818bhq` FOREIGN KEY (`quiz_form`) REFERENCES `quiz_form` (`id`);

--
-- Contraintes pour la table `notification`
--
ALTER TABLE `notification`
  ADD CONSTRAINT `FK1f1bu1cfyn2r3acihyfmy6mp8` FOREIGN KEY (`etudiant`) REFERENCES `utilisateur` (`id`);

--
-- Contraintes pour la table `paiement`
--
ALTER TABLE `paiement`
  ADD CONSTRAINT `FK44wofahyjxe5pc0l2iwif6nwe` FOREIGN KEY (`etudiant`) REFERENCES `utilisateur` (`id`);

--
-- Contraintes pour la table `quiz_form`
--
ALTER TABLE `quiz_form`
  ADD CONSTRAINT `FKlpe5ackwdmfcd5frsn2mdi2sw` FOREIGN KEY (`support_cours_id`) REFERENCES `support_cours` (`id`);

--
-- Contraintes pour la table `quiz_proposition`
--
ALTER TABLE `quiz_proposition`
  ADD CONSTRAINT `FK4rfnns0mi6e0rnrgipup5k8m3` FOREIGN KEY (`quiz_question`) REFERENCES `quiz_question` (`id`);

--
-- Contraintes pour la table `quiz_question`
--
ALTER TABLE `quiz_question`
  ADD CONSTRAINT `FKp0p77bvjfmq0lac20jtcte0gr` FOREIGN KEY (`quiz_form_id`) REFERENCES `quiz_form` (`id`);

--
-- Contraintes pour la table `quiz_response`
--
ALTER TABLE `quiz_response`
  ADD CONSTRAINT `FK597vf1qly7602yh35p3o4oaig` FOREIGN KEY (`proposition`) REFERENCES `quiz_proposition` (`id`),
  ADD CONSTRAINT `FK9cgjur228byqy6cmhwlgne7r9` FOREIGN KEY (`etudiant`) REFERENCES `utilisateur` (`id`),
  ADD CONSTRAINT `FKoq3dndstt99fyh3ggnvhswdt7` FOREIGN KEY (`question`) REFERENCES `quiz_question` (`id`);

--
-- Contraintes pour la table `reclamation`
--
ALTER TABLE `reclamation`
  ADD CONSTRAINT `FK534vqoh6tsbl2u4cfr51tptj3` FOREIGN KEY (`etudiant`) REFERENCES `utilisateur` (`id`);

--
-- Contraintes pour la table `support_cours`
--
ALTER TABLE `support_cours`
  ADD CONSTRAINT `FK274fj851wlt1q1q5y28jjyi9l` FOREIGN KEY (`cours_id`) REFERENCES `cours` (`id`),
  ADD CONSTRAINT `FK35bfqvnh0wgm5i0ewl6a73bw3` FOREIGN KEY (`enseignant_id`) REFERENCES `utilisateur` (`id`);

--
-- Contraintes pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  ADD CONSTRAINT `FK9goonys6bmu8wnxcqiaipafni` FOREIGN KEY (`classe`) REFERENCES `classe` (`id`),
  ADD CONSTRAINT `FKnjr7pd4ym4xrqw0t8aaupbp23` FOREIGN KEY (`parent`) REFERENCES `utilisateur` (`id`),
  ADD CONSTRAINT `FKqabmp7jj1n7basos78hkjoroo` FOREIGN KEY (`etudiant`) REFERENCES `utilisateur` (`id`);

--
-- Contraintes pour la table `verification_token`
--
ALTER TABLE `verification_token`
  ADD CONSTRAINT `FKk86b43hyrsarlu0vi15wepxaa` FOREIGN KEY (`user_id`) REFERENCES `utilisateur` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
