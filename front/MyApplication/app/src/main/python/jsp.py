import re
from pathlib import Path

CURRENT_DIR = Path(__file__).parent
ENTITY_DIR = CURRENT_DIR / "entity"

ENTITY_DIR.mkdir(exist_ok=True)


def remove_comments(content: str) -> str:
    # Supprimer commentaires bloc (/* ... */ et /** ... */)
    content = re.sub(
        r'/\*[\s\S]*?\*/',
        '',
        content
    )

    # Supprimer commentaires ligne (// ...)
    content = re.sub(
        r'^\s*//.*$',
        '',
        content,
        flags=re.MULTILINE
    )

    return content


def transform_file(content: str) -> str:
    # 1. Supprimer TOUS les commentaires
    content = remove_comments(content)

    # 2. Supprimer @file:Suppress
    content = re.sub(
        r'@file:Suppress\([\s\S]*?\)\n*',
        '',
        content
    )

    # 3. Supprimer toutes les annotations
    content = re.sub(
        r'^\s*@.*\n',
        '',
        content,
        flags=re.MULTILINE
    )

    # 4. Renommer data class -> Entity
    content = re.sub(
        r'\bdata class (\w+)\b',
        r'data class \1Entity',
        content
    )

    # 5. Simplifier les types kotlin.*
    replacements = {
        'kotlin.String': 'String',
        'kotlin.Int': 'Int',
        'kotlin.Boolean': 'Boolean',
        'kotlin.Long': 'Long',
        'kotlin.Double': 'Double',
        'kotlin.Float': 'Float'
    }

    for old, new in replacements.items():
        content = content.replace(old, new)

    # 6. Supprimer imports kotlinx.serialization
    content = re.sub(
        r'import kotlinx\.serialization\.[^\n]+\n',
        '',
        content
    )

    # Nettoyage lignes vides multiples
    content = re.sub(r'\n{3,}', '\n\n', content)

    return content.strip() + "\n"


def generate_entities():
    kt_files = [
        f for f in CURRENT_DIR.glob("*.kt")
        if not f.name.endswith("Entity.kt")
    ]

    if not kt_files:
        print("Aucun fichier .kt trouve")
        return

    for kt_file in kt_files:
        content = kt_file.read_text(encoding="utf-8")

        if not re.search(r'\bdata class\b', content):
            print(f"Ignore : {kt_file.name} (pas une data class)")
            continue

        entity_content = transform_file(content)
        output_file = ENTITY_DIR / kt_file.name.replace(".kt", "Entity.kt")

        output_file.write_text(entity_content, encoding="utf-8")
        print(f"GENERE : entity/{output_file.name}")


if __name__ == "__main__":
    generate_entities()
