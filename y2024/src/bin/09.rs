use adv_code_2024::*;
use anyhow::*;
use code_timing_macros::time_snippet;
use const_format::concatcp;
use itertools::Itertools;
use std::fs::File;
use std::io::{BufRead, BufReader};

const DAY: &str = "09"; // TODO: Fill the day
const INPUT_FILE: &str = concatcp!("input/", DAY, ".txt");

const TEST: &str = "2333133121414131402"; // TODO: Add the test input

fn main() -> Result<()> {
    start_day(DAY);

    //region Part 1
    println!("=== Part 1 ===");

    fn part1<R: BufRead>(reader: R) -> Result<usize> {
        let line = reader.lines().next().unwrap()?;
        let mut disk: Vec<String> = vec![];

        let mut id = 0;
        line.chars().enumerate().for_each(|(i, c)| {
            let count = c.to_digit(10).unwrap() as usize;
            match i % 2 {
                0 => {
                    disk.extend(vec![id.to_string(); count]);
                    id += 1;
                }
                _ => disk.extend(vec![".".to_string(); count]),
            }
        });

        // defrag, iterate left/right
        let mut left = disk.iter().position(|x| x == ".").unwrap();
        let mut right = disk.len() - 1;
        while left < right {
            let val = &disk[right];
            match val.as_str() {
                "." => right -= 1,
                _ => {
                    disk[left] = val.to_string();
                    disk[right] = ".".to_string();
                    left = disk.iter().position(|x| x == ".").unwrap();
                    right -= 1
                }
            }
        }

        // checksum
        let answer = checksum(&mut disk);

        Ok(answer)
    }
    fn checksum(disk: &mut Vec<String>) -> usize {
        let answer = disk
            .iter()
            .enumerate()
            .map(|(i, &ref val)| match val.as_str() {
                "." => 0,
                _ => {
                    let val = val.parse::<usize>().unwrap();
                    i * val
                }
            })
            .collect_vec()
            .into_iter()
            .sum();
        answer
    }

    // TODO: Set the expected answer for the test input
    assert_eq!(1928, part1(BufReader::new(TEST.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part1(input_file)?);
    println!("Result = {}", result);
    //endregion

    //region Part 2
    println!("\n=== Part 2 ===");

    fn part2<R: BufRead>(reader: R) -> Result<usize> {
        let line = reader.lines().next().unwrap()?;
        let mut disk: Vec<Vec<String>> = vec![];
        let mut id = 0;
        line.chars().enumerate().for_each(|(i, c)| {
            let count = c.to_digit(10).unwrap() as usize;
            if count > 0 {
                match i % 2 {
                    0 => {
                        disk.push(vec![id.to_string(); count]);
                        id += 1;
                    }
                    _ => disk.push(vec![".".to_string(); count]),
                }
            }
        });

        id -= 1;
        // defrag
        while id > 0 {
            let needle = id.to_string();
            let right = disk.iter().rposition(|x| x[0] == needle).unwrap();
            let file = &disk[right];
            let filesize = file.len();

            match disk.iter().position(|x| x[0] == "." && x.len() >= filesize) {
                Some(insert) => {
                    if insert < right {
                        let remaining = disk[insert].len() - filesize;
                        disk[insert] = disk[right].clone();
                        disk[right] = vec![".".to_string(); filesize];
                        if remaining > 0 {
                            disk.insert(insert + 1, vec![".".to_string(); remaining]);
                        }
                    }
                }
                _ => {}
            }
            id -= 1;
        }

        let mut flat_disk = disk.iter().flatten().map(|v| v.to_string()).collect_vec();
        let answer = checksum(&mut flat_disk);

        Ok(answer)
    }

    assert_eq!(2858, part2(BufReader::new(TEST.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part2(input_file)?);
    println!("Result = {}", result);
    //endregion

    Ok(())
}
