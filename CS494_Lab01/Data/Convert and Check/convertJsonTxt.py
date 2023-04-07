import csv
import json

# Load JSON data from file
with open('Data03.json', 'r') as f:
    data = json.load(f)

# Open CSV file for writing
with open('Data03.csv', 'w', newline='') as f:
    writer = csv.writer(f)

    # Write headers
    writer.writerow(['question id', 'question', 'a', 'b', 'c', 'd', 'solution'])

    # Write data rows
    for i, question in enumerate(data, start=1):
        writer.writerow([
            i,
            question['question'],
            question['answers']['a'],
            question['answers']['b'],
            question['answers']['c'],
            question['answers']['d'],
            question['solution']
        ])

print('Conversion complete.')
