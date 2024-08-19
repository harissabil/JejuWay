import pandas as pd
from bs4 import BeautifulSoup
import requests
from tqdm import tqdm  # For progress bar
import time
import random

# Load the DataFrame from your CSV or other sources
# For example, if you have the DataFrame saved as a CSV file:
df = pd.read_csv('jeju_itinerary_with_content_no_esc_2.csv')

# ambil yang non menganadung 'Failed' dan error dan nan
df = df[df['Content'].str.contains('Failed|Error', na=False) == False]

# ambil hanya yang pada kolom content mengandung 'Failed' dan error dan nan
df = df[df['Content'].str.contains('Failed|Error', na=False) == True]

# hapus isi kolom content
df['Content'] = ''

# Function to extract content from a webpage
def extract_content(url):
    try:
        headers = {
            "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
        }
        response = requests.get(url, headers=headers, timeout=10)
        if response.status_code == 200:
            soup = BeautifulSoup(response.content, 'html.parser')
            # Extract main content (this can be adjusted depending on the structure of the page)
            paragraphs = soup.find_all('p')
            content = ' '.join([para.get_text() for para in paragraphs])
            return content.strip()
        else:
            return "Failed to retrieve content"
    except Exception as e:
        return f"Error: {e}"

# Add a new column to the DataFrame for the extracted content
df['Content'] = ''

# Iterate over each link in the DataFrame and extract the content
for idx, row in tqdm(df.iterrows(), total=df.shape[0], desc="Extracting Web Content"):
    link = row['Link']
    content = extract_content(link)
    df.at[idx, 'Content'] = content
    
    # Introduce a random delay to avoid getting blocked
    time.sleep(random.uniform(2, 5))

# clean escape characters
def clean_text(text):
    return text.replace('\n', ' ').replace('\r', ' ').replace('\t', ' ')

df['Content'] = df['Content'].apply(clean_text)

import csv

# Save the DataFrame with the new content column to a CSV file
df.to_csv('jeju_itinerary_with_content_no_esc_2.csv', index=False, escapechar='\\', quoting=csv.QUOTE_MINIMAL)

# Display the first few rows of the updated DataFrame
print(df.head())

# load jeju itinerary with content no esc 1 
df1 = pd.read_csv('jeju_itinerary_with_content_no_esc.csv')

# replace df1 sesuai dengan link dengan df
df1 = df1[df1['Link'].isin(df['Link']) == False]

# hapus df1 yang mengandung 'Failed' dan error dan nan
df1 = df1[df1['Content'].str.contains('Failed|Error', na=False) == False]

# hapus yang kosong
df1 = df1.dropna(subset=['Content'])

# hapus yang kurang dari 10 kata
df1 = df1[df1['Content'].apply(lambda x: len(x.split()) >= 30)]

df1.to_csv('jeju_itinerary_with_content_clean.csv', index=False, escapechar='\\', quoting=csv.QUOTE_MINIMAL)