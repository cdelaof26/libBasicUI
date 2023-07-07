from sys import argv
from pathlib import Path
from os import name
import base64


argv.pop(0)
if not argv:
	print("Missing path")
	exit(1)

file_name = argv[0]
if name == "posix":
	file_name = file_name.replace("\\", "")
file_name = Path(file_name)


def read_write(file_name: Path):
	with open(file_name, "rb") as img_file:
		data = f"{base64.b64encode(img_file.read())}"[2:][:-1]

	new_file = file_name.name.replace(file_name.suffix, "") + ".txt"

	with open(file_name.with_name(new_file), "w") as file:
		file.write(data)


if file_name.is_file():
	read_write(file_name)
elif file_name.is_dir():
	directories = [file_name]
	
	while directories:
		dir = directories[0]
		for element in dir.iterdir():
			if element.is_file():
				print(f"Process {element.name}?")
				print("1. Yes\n2. No")
				if input("> ") == "1":
					read_write(element)
			elif element.is_dir():
				directories.append(element)

		directories.pop(0)
