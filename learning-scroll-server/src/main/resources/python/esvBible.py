import requests
import time

# Your ESV API Key
API_KEY = "249608ae928525b29b84dda6b54221e0ec74ad04"
API_URL = "https://api.esv.org/v3/passage/text/"

# List of passages to retrieve
passages = [
    "Genesis 1:1–31",
    "Genesis 3:1–24",
    "Genesis 6:5–22",
    "Genesis 7:1–24",
    "Genesis 12:1–9",
    "Exodus 3:1–12",
    "Exodus 14:1–31",
    "Exodus 16:1–36",
    "Joshua 6:1–27",
    "Judges 7:1–25",
    "1 Samuel 3:1–21",
    "1 Samuel 17:1–58",
    "Matthew 4:1–11",
    "Matthew 5:1–12",
    "Luke 10:25–37",
    "Acts 2:1–41",
    "Acts 9:1–22",
    "1 Kings 18:20–40"
]

# API Request Parameters
params = {
    "include-headings": False,
    "include-footnotes": False,
    "include-verse-numbers": False,
    "include-short-copyright": False,
    "include-passage-references": False
}

# Headers with API Key
headers = {
    "Authorization": f"Token {API_KEY}"
}

def fetch_passage_text(passage):
    """Fetches the passage text from the ESV API"""
    try:
        response = requests.get(API_URL, params={**params, "q": passage}, headers=headers)
        response.raise_for_status()
        data = response.json()
        return data["passages"][0].strip() if "passages" in data else "Error: Passage not found"
    except requests.exceptions.RequestException as e:
        return f"Error fetching {passage}: {e}"

# Fetch passages and store results
passage_texts = {}

for passage in passages:
    print(f"Fetching: {passage} ...")
    passage_texts[passage] = fetch_passage_text(passage)
    time.sleep(1)  # Delay to avoid hitting API rate limits

# Print or Save Results
for passage, text in passage_texts.items():
    print(f"\n{passage}:\n{text}\n{'='*40}")
