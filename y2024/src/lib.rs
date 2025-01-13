use std::io::BufRead;

pub fn start_day(day: &str) {
    println!("Advent of Code 2024 - Day {:0>2}", day);
}

// Additional common functions
pub fn read_to_2d<R: BufRead>(reader: R) -> Vec<Vec<char>> {
    let grid: Vec<Vec<_>> = reader
        .lines()
        .map(|line| line.unwrap().chars().collect::<Vec<_>>())
        .collect();
    grid
}

pub fn read_to_blocks<R: BufRead>(reader: R) -> Vec<Vec<String>> {
    let mut lines = reader.lines().peekable();
    let mut blocks = vec![];
    while lines.peek().is_some() {
        let block: Vec<_> = lines
            .by_ref()
            .take_while(|l| {
                let x = l.as_ref().unwrap();
                x.trim().len() > 0
            })
            .map(|x| x.unwrap().to_string())
            .collect();
        blocks.push(block);
    }
    blocks
}

pub fn find(haystack: &Vec<Vec<char>>, needle: char) -> (usize, usize) {
    let rows = haystack.len();
    let cols = haystack[0].len();

    for i in 0..rows {
        for j in 0..cols {
            if haystack[i][j] == needle {
                return (i, j);
            }
        }
    }
    panic!("not found");
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn it_works() {
        start_day("00");
    }
}
