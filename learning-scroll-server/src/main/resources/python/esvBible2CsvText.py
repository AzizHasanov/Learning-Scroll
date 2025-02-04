import requests
import csv

API_KEY = "249608ae928525b29b84dda6b54221e0ec74ad04"
API_URL = "https://api.esv.org/v3/passage/text/"
OUTPUT_FILE = "bible_passages.csv"  # CSV output file

def get_esv_text(passage):
    params = {
        'q': passage,
        'include-headings': False,
        'include-footnotes': False,
        'include-verse-numbers': False,
        'include-short-copyright': False,
        'include-passage-references': False
    }

    headers = {
        'Authorization': f'Token {API_KEY}'
    }

    response = requests.get(API_URL, params=params, headers=headers)

    if response.status_code == 200:
        passages = response.json().get('passages', [])
        if passages:
            # Format: Remove new lines and extra spaces, then wrap in triple stars ***
            formatted_text = ' '.join(passages[0].split()).strip()
            return f'***{formatted_text}***'
    return "Error: Passage not found"

# List of passages to retrieve
bible_passages = [
    "1 Kings 18:20–40",
    "Acts 9:1–22",
    "Genesis 1:1–31",
    "Genesis 7:1–24",
    "Exodus 14:1–31",
    "Exodus 16:1–36",
    "Deuteronomy 34:1–8",
    "Joshua 6:1–27",
    "Judges 7:1–25",
    "1 Samuel 17:1–58",
    "2 Kings 5:1–14",
    "Matthew 14:22–33",
    "Acts 16:16–40",
    "Genesis 11:1–9",
    "Numbers 21:4–9",
    "Acts 2:1–41",
    "Genesis 3:1–24",
    "Luke 15:11–32",
    "Acts 1:8",
    "Exodus 14:21–31",
    "Exodus 16:13–31",
    "Jonah 1:1–17",
    "Romans 8:28",
    "1 Kings 18:21",
    "Isaiah 40:31",
    "Matthew 5:13",
    "2 Kings 2:1–12",
    "Luke 10:25–37",
    "Acts 16:25–34",
    "Luke 19:1–10",
    "Genesis 4:1–16",
    "Genesis 12:1–9",
    "Judges 16:15–22",
    "John 3:1–21",
    "2 Samuel 11:1–27",
    "Psalm 23:1–6",
    "Matthew 5:1–12",
    "Exodus 12:1–30 ",
    "Isaiah 53:1–7",
    "John 6:35",
    "Acts 2:1–4 ",
    "Psalm 23:1 ",
    "John 15:5",
    "Genesis 22:1–19",
    "Numbers 13:25–33",
    "1 Samuel 16:1–13",
    "Isaiah 6:1–8",
    "Jonah 4:1–11",
    "Matthew 8:23–27",
    "John 11:1–44",
    "Genesis 37:1–36",
    "Job 2:1–10",
    "Jonah 3:1–10",
    "Acts 9:1–19",
    "Genesis 3:1–5",
    "Exodus 32:1–6",
    "Acts 5:27–42",
    "Genesis 4:1–12",
    "1 Kings 3:16–28",
    "Deuteronomy 6:4",
    "1 Samuel 8:4–22",
    "Matthew 26:59–66",
    "Nehemiah 6:1–16",
    "Esther 4:1–17",
    "Job 1:1–22",
    "Isaiah 53:1–12",
    "Revelation 21:1–8",
    "Exodus 3:1–12",
    "Judges 6:11–24",
    "Genesis 3:1–7",
    "1 Samuel 3:1–21",
    "Matthew 4:1–11",
    "Jonah 1:17",
    "Exodus 1:8–22",
    "Matthew 22:15–22",
    "Acts 17:16–34",
    "Genesis 3:15",
    "Deuteronomy 18:15–19",
    "2 Samuel 7:12–16",
    "Isaiah 7:14",
    "Exodus 14:10–31",
    "Romans 8:28–39",
    "Micah 5:2",
    "Zechariah 9:9",
    "Zechariah 11:12–13",
    "Psalm 22:1–18",
    "Daniel 7:13–14",
    "Hosea 11:1",
    "Malachi 3:1",
    "Luke 24:44",
    "Deuteronomy 6:4–9",
    "Matthew 13:1–23"
]

# Writing to CSV file
with open(OUTPUT_FILE, mode='w', newline='', encoding='utf-8') as file:
    writer = csv.writer(file)
    writer.writerow(["Passage Address", "Passage Text"])  # Writing headers

    for passage in bible_passages:
        formatted_passage = get_esv_text(passage)
        writer.writerow([passage, formatted_passage])

print(f"CSV file '{OUTPUT_FILE}' has been created successfully!")
