 #!/bin/bash
echo "running " $@
sleep 5

while [[ $# -gt 0 ]]
do
key="$1"

case $key in
    --file)
    FILE="$2"
    shift # past argument
    shift # past value
    ;;
    --text)
    PATTERN="$2"
    shift # past argument
    shift # past value
    ;;
    *)    # unknown option
    shift # past argument
    ;;
esac
done

grep $PATTERN $FILE > output
