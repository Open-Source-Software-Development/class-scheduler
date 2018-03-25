#!/bin/bash

ERRORS=0
sed -e 's/Hunk(\(.*\))/\1/' -e 's/, /,/g' | while IFS=, read -r SECTION PROFESSOR ROOM BLOCKS; do
    COURSE=$(echo "$SECTION" | grep '^[^-]*-[^-]*' -o)
    if ! grep "$PROFESSOR" <userconstraint.csv | grep "$COURSE" -q; then
        echo "INVALID: $PROFESSOR can't teach $COURSE"
        ERRORS=$(($ERRORS + 1))
    fi
done

echo "Completed with $ERRORS errors"
exit $ERRORS
