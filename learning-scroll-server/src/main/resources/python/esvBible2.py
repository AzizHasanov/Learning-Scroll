import requests

API_KEY = "249608ae928525b29b84dda6b54221e0ec74ad04"
API_URL = "https://api.esv.org/v3/passage/text/"

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
            # Format: Remove new lines and extra spaces, then wrap in triple quotes
            formatted_text = ' '.join(passages[0].split()).strip()
            return f'"""{formatted_text}"""'
    return "Error: Passage not found"

# Example usage
bible_passages = [
    "Genesis 1:1–31",
    "Genesis 3:1–24",
    "Genesis 6:5–22"
]

for passage in bible_passages:
    print(f"{passage}: {get_esv_text(passage)}\n")
