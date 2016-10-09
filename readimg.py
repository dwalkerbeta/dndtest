import sys
import os
from subprocess import check_output

url = raw_input('Enter a URL: ')

out = check_output(['java', '-jar', 'app.jar', url])

print("\n" + out)