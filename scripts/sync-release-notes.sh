#!/usr/bin/env bash
#
# Sync release-notes-*.txt (root repo, kamu yang edit manual per rilis)
# ke folder release-notes GPP sebelum publish ke Play Store.
#
# Cara pakai:
#   ./scripts/sync-release-notes.sh
#
# Sumber : release-notes-id.txt -> locale id,
#          release-notes-en.txt -> locale en-US (lihat NOTES_MAP di bawah).
# Output : app/src/main/play/release-notes/{id,en-US}/default.txt
#          (maks 500 karakter sesuai limit Play Store, kelebihan dipotong)
#
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
# Sumber notes per bahasa (kamu yang edit manual per rilis).
# Format: "<locale>:<file>" dipisah spasi. Ubah/tambah sesuai kebutuhan.
NOTES_MAP="id:$ROOT/release-notes-id.txt en-US:$ROOT/release-notes-en.txt"
MAX_CHARS=500

for pair in $NOTES_MAP; do
  locale="${pair%%:*}"
  SRC="${pair#*:}"
  [[ -f "$SRC" ]] || { echo "File notes tidak ditemukan: $SRC"; exit 1; }

  NOTES="$(cat "$SRC")"
  LEN="${#NOTES}"
  if (( LEN > MAX_CHARS )); then
    echo "WARNING [$locale]: $LEN karakter, melebihi limit Play ($MAX_CHARS). Dipotong."
    NOTES="${NOTES:0:$MAX_CHARS}"
  fi

  dir="$ROOT/app/src/main/play/release-notes/$locale"
  mkdir -p "$dir"
  printf '%s' "$NOTES" > "$dir/default.txt"
  echo "OK: $dir/default.txt (${#NOTES} karakter)"
done
