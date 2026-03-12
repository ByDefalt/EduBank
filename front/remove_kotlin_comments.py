#!/usr/bin/env python3
"""
Script pour supprimer les commentaires inline (//) dans les fichiers Kotlin (.kt)
Chaque suppression est confirmée interactivement.
Usage: python remove_kotlin_comments.py <dossier>
"""

import sys
import argparse
from pathlib import Path


# ─── Couleurs ANSI ────────────────────────────────────────────────────────────
RED    = "\033[31m"
GREEN  = "\033[32m"
YELLOW = "\033[33m"
CYAN   = "\033[36m"
BOLD   = "\033[1m"
DIM    = "\033[2m"
RESET  = "\033[0m"


def strip_inline_comment(line: str) -> str | None:
    """
    Retourne la ligne sans son commentaire // si elle en contient un,
    sinon retourne None (= pas de commentaire à supprimer).
    Ignore les // dans les strings (double quotes, triple quotes, single quotes).
    """
    in_single_quote = False
    in_double_quote = False
    in_triple_quote = False
    i = 0

    while i < len(line):
        c = line[i]

        # Triple quote Kotlin : """ ... """
        if not in_single_quote and not in_triple_quote and line[i:i+3] == '"""':
            in_triple_quote = True
            i += 3
            continue
        if in_triple_quote:
            if line[i:i+3] == '"""':
                in_triple_quote = False
                i += 3
            else:
                i += 1
            continue

        # Échappement dans une string
        if c == '\\' and in_double_quote:
            i += 2
            continue

        # Double quote
        if c == '"' and not in_single_quote:
            in_double_quote = not in_double_quote
            i += 1
            continue

        # Single quote
        if c == "'" and not in_double_quote:
            in_single_quote = not in_single_quote
            i += 1
            continue

        # Commentaire inline détecté
        if not in_double_quote and not in_single_quote:
            if line[i:i+2] == '//':
                stripped = line[:i].rstrip()
                # Ne signaler que si la ligne change réellement
                return stripped if stripped != line else None

        i += 1

    return None  # pas de commentaire


def confirm_deletion(filepath_display: str, lineno: int, original: str, stripped: str) -> bool:
    """
    Affiche la ligne avant/après et demande confirmation.
    Entrée vide = OUI, 'n' = NON.
    Retourne True si l'utilisateur valide la suppression.
    """
    print(f"\n  {CYAN}{BOLD}Fichier{RESET} {filepath_display}  {DIM}ligne {lineno}{RESET}")
    print(f"  {RED}  - {original}{RESET}")
    print(f"  {GREEN}  + {stripped if stripped else '(ligne vide)'}{RESET}")

    try:
        answer = input(f"  {YELLOW}Supprimer ? [Entrée=oui / n=non] :{RESET} ").strip().lower()
    except (EOFError, KeyboardInterrupt):
        print(f"\n{YELLOW}Interruption — arrêt du programme.{RESET}")
        sys.exit(0)

    return answer != 'n'


def process_file(filepath: Path, root: Path) -> tuple[int, int]:
    """
    Traite un fichier .kt ligne par ligne avec confirmation interactive.
    Retourne (nb_validés, nb_refusés).
    """
    try:
        content = filepath.read_text(encoding='utf-8')
    except UnicodeDecodeError:
        try:
            content = filepath.read_text(encoding='latin-1')
        except Exception as e:
            print(f"  ⚠️  Impossible de lire {filepath}: {e}")
            return 0, 0

    lines = content.split('\n')
    new_lines = []
    validated = 0
    skipped = 0
    has_change = False
    filepath_display = str(filepath.relative_to(root))

    for lineno, line in enumerate(lines, start=1):
        stripped = strip_inline_comment(line)

        if stripped is not None:
            if confirm_deletion(filepath_display, lineno, line, stripped):
                new_lines.append(stripped)
                validated += 1
                has_change = True
            else:
                new_lines.append(line)
                skipped += 1
        else:
            new_lines.append(line)

    if has_change:
        filepath.write_text('\n'.join(new_lines), encoding='utf-8')

    return validated, skipped


def process_directory(root_dir: str):
    root_path = Path(root_dir)

    if not root_path.exists():
        print(f"❌ Dossier introuvable : {root_dir}")
        sys.exit(1)
    if not root_path.is_dir():
        print(f"❌ Ce n'est pas un dossier : {root_dir}")
        sys.exit(1)

    kt_files = sorted(root_path.rglob('*.kt'))

    if not kt_files:
        print(f"Aucun fichier .kt trouvé dans {root_dir}")
        sys.exit(0)

    print(f"{BOLD}📁 Dossier cible :{RESET} {root_path.resolve()}")
    print(f"{BOLD}{len(kt_files)} fichier(s) .kt trouvé(s){RESET}")
    print(f"{DIM}[Entrée] = valider la suppression   [n + Entrée] = garder le commentaire{RESET}")
    print("─" * 60)

    total_validated = 0
    total_skipped = 0
    files_modified = 0

    for kt_file in kt_files:
        validated, skipped = process_file(kt_file, root_path)
        total_validated += validated
        total_skipped += skipped
        if validated > 0:
            files_modified += 1

    print(f"\n{'═' * 60}")
    print(f"{BOLD}📊 Résumé final :{RESET}")
    print(f"   Fichiers modifiés      : {files_modified} / {len(kt_files)}")
    print(f"   Commentaires supprimés : {GREEN}{total_validated}{RESET}")
    print(f"   Commentaires conservés : {YELLOW}{total_skipped}{RESET}")


def main():
    parser = argparse.ArgumentParser(
        description='Supprime interactivement les commentaires inline (//) dans les fichiers Kotlin'
    )
    parser.add_argument('dossier', help='Chemin du dossier racine du projet Kotlin')
    args = parser.parse_args()
    process_directory("./MyApplication")


if __name__ == '__main__':
    main()
