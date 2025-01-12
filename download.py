import requests
import os

def download_images(suffix_list):
  """
  Downloads images from an API given a list of suffixes.

  Args:
    suffix_list: A list of strings representing the suffixes for the API.
    api_base_url: The base URL of the API.

  Returns:
    None
  """

  for suffix in suffix_list:
    try:
      # Construct the API URL
      api_url = f"https://dog.ceo/api/breed/{suffix}/images/random"

      # Make the API request
      response = requests.get(api_url)
      response.raise_for_status()  # Raise an exception for bad status codes

      # Extract the image URL from the response
      image_url = response.json()["message"]  # Assuming the response is JSON

      # Download the image
      image_data = requests.get(image_url).content
      image_path = f"images/{suffix.replace("/", "_")}.jpg"  # Example filename
      os.makedirs(os.path.dirname(image_path), exist_ok=True)  # Create directories if needed
      with open(image_path, 'wb') as f:
        f.write(image_data)

      print(f"Downloaded image for suffix: {suffix}")

    except requests.exceptions.RequestException as e:
      print(f"Error downloading image for suffix {suffix}: {e}")
    except KeyError as e:
      print(f"Error: 'imageUrl' key not found in API response for suffix {suffix}")

# Example usage
suffix_list = [
"mastiff/bull",
"mastiff/english",
"mastiff/indian",
"mastiff/tibetan",
]

download_images(suffix_list)