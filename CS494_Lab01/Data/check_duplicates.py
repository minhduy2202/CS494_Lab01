import json

with open('Data03.json') as f:
    questions = json.load(f)

seen_questions = set()
duplicates = []

for q in questions:
    if q['question'] in seen_questions:
        duplicates.append(q['question'])
    else:
        seen_questions.add(q['question'])

if duplicates:
    print("Duplicate questions found:")
    for d in duplicates:
        print(d)
else:
    print("No duplicate questions found.")

print(len(seen_questions))